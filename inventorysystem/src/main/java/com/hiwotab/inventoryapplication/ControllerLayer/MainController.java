package com.hiwotab.inventoryapplication.ControllerLayer;


import com.hiwotab.inventoryapplication.ModelLayer.Product;
import com.hiwotab.inventoryapplication.ModelLayer.Transaction;
import com.hiwotab.inventoryapplication.RepositoryLayer.ProductRepository;
import com.hiwotab.inventoryapplication.RepositoryLayer.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MainController
{
    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/")
    public String showHomePages()
    {
        return "index";
    }
    @GetMapping("/homePage")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/addProductInfo")
    public String addProductInfos(Model model)
    {
         model.addAttribute("newProd", new Product());
         return "addProductInfo";
    }
    @PostMapping("/addProductInfo")
    public String addProductInfos(@Valid @ModelAttribute("newProd") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addProductInfo";
        }

        productRepository.save(product);
        return "dispProdcutInfo";
    }

    @GetMapping("/searchProdCode")
    public String searchChefMethod(Model model){
        model.addAttribute("searchCode",new Transaction());
        return "searchProdCode";
    }
    @PostMapping("/searchProdCode")
    public String searchChefMethod( @ModelAttribute("searchCode")Transaction transaction,Model model, BindingResult bindingResult) {
        //Product product = productRepository.findOne(transaction.getProdCode());


        if(bindingResult.hasErrors()) {
            return "searchProdCode";
        }
        Iterable<Product>productlist=productRepository.findAllByProdCode(transaction.getProdCode());
        Product product=productlist.iterator().next();
        if (transaction.getProdCode() ==null) {
            String message = "Sorry!! Product with this Code not found Please try another";
            model.addAttribute("showNoPrdctMsg", true);
            model.addAttribute("nocode", message);
            return "searchProdCode";
        }
        transaction.setProdCode(product.getProdCode());
        transaction.setProdName(product.getProdName());
        transaction.setQuantity(product.getQuantity());
        transaction.setPrice(product.getPrice());
        double tax = 0.06;
        transaction.setTaxTotal(product.getPrice() * tax * transaction.getQuantity());
        transaction.setTotalPrice(transaction.getTaxTotal() + transaction.getQuantity() * product.getPrice());
        transactionRepository.save(transaction);

        return "dispReciept";
    }
    @GetMapping("/dispAllProduct")
    public String dispAllProdInfo(Model model) {
        Iterable<Product> prodList= productRepository.findAll();
        model.addAttribute("prodList", prodList);

        return "dispAllProduct";

    }
    @GetMapping("/dispAllTransaction")
    public String dispAllTransInfo(Model model) {
        Iterable<Transaction> transactionList= transactionRepository.findAll();
        model.addAttribute("searchTrans", transactionList);

        return "dispAllTransaction";

    }
    @GetMapping("/dispProdQaulity")
    public String dispProdQaulityInfo(Model model) {

        Iterable<Product> prodPos = productRepository.findAllByQuantityGreaterThan(0);
        model.addAttribute("prodPos", prodPos);

        Iterable<Product> prodNeg = productRepository.findAllByQuantityLessThan(0);
        model.addAttribute("prodNeg", prodNeg);

        return "dispProdQaulity";
    }

}
