package com.bookworm.controller;

import com.bookworm.domain.Book;
import com.bookworm.domain.CartItem;
import com.bookworm.domain.ShoppingCart;
import com.bookworm.domain.User;
import com.bookworm.service.BookService;
import com.bookworm.service.CartItemService;
import com.bookworm.service.ShoppingCartService;
import com.bookworm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * @author rams0516
 *         Date: 1/29/2018
 *         Time: 4:48 PM
 */

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private BookService bookService;

    @RequestMapping("/cart")
    public String shoppingCart(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<CartItem> cartItems = cartItemService.findByShoppingCart(shoppingCart);
        shoppingCartService.updateShoppingCart(shoppingCart);

        model.addAttribute("cartItemList", cartItems);
        model.addAttribute("shoppingCart", shoppingCart);
        return "shoppingCart";
    }

    @RequestMapping("addItem")
    public String addItem(
            @ModelAttribute("book") Book book,
            @ModelAttribute("qty") String qty,
            Principal principal, Model model
            ) {
        User user = userService.findByUsername(principal.getName());
        book = bookService.findOne(book.getId());

        if(Integer.parseInt(qty) > book.getInStockNumber()) {
            model.addAttribute("notEnoughStock", true);
            return "forward:/bookDetail?id="+book.getId();
        }

        CartItem cartItem = cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
        model.addAttribute("addBookSuccess", true);
        return "forward:/bookDetail?id="+book.getId();
    }

    @RequestMapping("/updateCartItem")
    public String updateCartItem(
            @ModelAttribute("id") Long id,
            @ModelAttribute("qty") int qty
    ) {
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQty(qty);
        cartItemService.updateCartItem(cartItem);

        return "forward:/shoppingCart/cart";
    }

    @RequestMapping("removeItem")
    public String removeItem(@RequestParam("id") Long id) {
        cartItemService.removeCartItem(cartItemService.findById(id));
        return "forward:/shoppingCart/cart";
    }
}