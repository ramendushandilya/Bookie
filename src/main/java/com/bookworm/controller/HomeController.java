package com.bookworm.controller;

import com.bookworm.domain.*;
import com.bookworm.domain.security.PasswordResetToken;
import com.bookworm.domain.security.Role;
import com.bookworm.domain.security.UserRole;
import com.bookworm.service.BookService;
import com.bookworm.service.UserPaymentService;
import com.bookworm.service.UserService;
import com.bookworm.service.UserShippingService;
import com.bookworm.service.impl.UserSecurityService;
import com.bookworm.utility.MailConstructor;
import com.bookworm.utility.SecurityUtility;
import com.bookworm.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;

/**
 * Created by failedOptimus on 14-01-2018.
 */

@Controller
public class HomeController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @RequestMapping("/")
    public String showIndex() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {

        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    /**
     * Handles the creation of new user and sending the mail for account activation
     * @param request
     * @param userEmail
     * @param userName
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUserPost(HttpServletRequest request,
                              @ModelAttribute("email") String userEmail,
                              @ModelAttribute("username") String userName,
                              Model model) throws Exception {
        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", userName);

        if(userService.findByUsername(userName) != null) {
            model.addAttribute("usernameExists", true);
            return "myAccount";
        }

        if(userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);
            return "myAccount";
        }

        User user = new User();
        user.setEmail(userEmail);
        user.setUsername(userName);

        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
        mailSender.send(email);

        model.addAttribute("emailSent", true);
        System.out.print("inside the check");
        return "myAccount";
    }

    /**
     * Handles the user creation final part once the user verifies account after clicking the email
     * @param model
     * @param locale
     * @param token
     * @return
     */
    @RequestMapping("/newUser")
    public String newUser(Model model, Locale locale, @RequestParam("token") String token) {
        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

        if(passwordResetToken == null) {
            String message = "Invalid Token";
            model.addAttribute("message", message);
            return "redirect:/badrequest";
        }

        User user = passwordResetToken.getUser();
        String username = user.getUsername();
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);
        model.addAttribute("classActiveEdit", true);
        return "myProfile";
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public String updateUserInfo(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {
        return null;
    }

    /**
     * Handles the part where user forgets password and wants to retrieve the details
     * @param request
     * @param email
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/forgetPassword")
    public String forgetPassword(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model
    ) throws Exception{
        model.addAttribute("classActiveForgetPassword", true);
        User user = userService.findByEmail(email);

        if(user == null) {
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();


        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
                password);

        //TODO enhance mail constructor to handle the request of forget password reset mail more explicitly

        mailSender.send(newEmail);
        model.addAttribute("forgetPasswordEmailSent", true);

        return "myAccount";
    }

    /**
     * Fetches the list of books from the backend to be shown to the user
     * @param model
     * @return
     */
    @RequestMapping("/bookshelf")
    public String bookshelf(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("bookList", books);
        return "bookshelf";
    }

    /**
     * Gets the details of a particular book by it's id
     * @param id
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/bookDetail")
    public String bookDetails(
            @PathParam("id") Long id,
            Model model,
            Principal principal
    ) {
        if(principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        List<Integer> quantity = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        model.addAttribute("qtyList", quantity);
        model.addAttribute("qty",1);
        return "bookDetail";
    }

    /**
     * Generates the profile section of the user
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddresses", true);
        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("classActiveEdit", true);
        return "myProfile";
    }

    @RequestMapping("/listOfCreditCards")
    public String listOfCreditCards(
            Model model, Principal principal, HttpServletRequest request
    ) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    @RequestMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(
            Model model, Principal principal, HttpServletRequest request
    ) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    @RequestMapping("/addNewCreditCard")
    public String addNewCreditCard(
            Model model, Principal principal
    ) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();


        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);

        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
    public String addNewCreditCard(
            @ModelAttribute("userPayment") UserPayment userPayment,
            @ModelAttribute("userBilling") UserBilling userBilling,
            Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
    public String addNewShippingAddressPost(
            @ModelAttribute("userShipping") UserShipping userShipping,
            Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping("/updateCreditCard")
    public String updateCreditCart(
            @ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getUserId() != userPayment.getUser().getUserId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userPayment", userPayment);
            model.addAttribute("userBilling", userBilling);

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);
            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping("/updateUserShipping")
    public String updateUserSipping(
            @ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if(user.getUserId() != userShipping.getUser().getUserId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("userShipping", userShipping);
            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
    public String setDefaultPayment(
            @ModelAttribute("defaultUserPaymentId") Long defaultUserPaymentId, Principal principal,
            Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(defaultUserPaymentId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddress", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
    public String setDefaultShippingAddress(
            @ModelAttribute("defaultShippingAddressId") Long id, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.setDefaultShippingAddress(id, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }


}