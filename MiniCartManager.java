import java.util.Scanner;

// Interface for discount
interface Discountable {
    double applyDiscount();
}

// Product class
class Product implements Discountable {
    private String name;
    private double price;

    Product(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName(){ return name; }
    public double getPrice(){ return price; }

    @Override
    public double applyDiscount(){
        return price; // no discount by default
    }

    public void display(){
        System.out.println("Product: " + name + ", Price: \u20B9" + price);
    }
}

// SpecialProduct with discount
class SpecialProduct extends Product {
    private double discountPercent;

    SpecialProduct(String name, double price, double discountPercent){
        super(name, price);
        this.discountPercent = discountPercent;
    }

    @Override
    public double applyDiscount(){
        double discountedPrice = getPrice() - (getPrice() * discountPercent / 100);
        return discountedPrice;
    }

    @Override
    public void display(){
        System.out.println("Special Product: " + getName() + ", Price after " + discountPercent + "% discount: \u20B9" + applyDiscount());
    }
}

// Cart class
class Cart {
    private Product p1, p2, p3, p4, p5;
    private int itemCount;

    Cart(){ itemCount = 0; }

    public boolean addProduct(Product p){
        if(itemCount == 5){
            System.out.println("Cart full! Can't add more products.");
            return false;
        }

        itemCount++;
        switch(itemCount){
            case 1: p1 = p; break;
            case 2: p2 = p; break;
            case 3: p3 = p; break;
            case 4: p4 = p; break;
            case 5: p5 = p; break;
        }
        System.out.println(p.getName() + " added to cart.");
        return true;
    }

    public void viewCart(){
        if(itemCount == 0){
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\n--- Your Cart Items ---");
        double total = 0;
        if(p1 != null){ p1.display(); total += p1.applyDiscount(); }
        if(p2 != null){ p2.display(); total += p2.applyDiscount(); }
        if(p3 != null){ p3.display(); total += p3.applyDiscount(); }
        if(p4 != null){ p4.display(); total += p4.applyDiscount(); }
        if(p5 != null){ p5.display(); total += p5.applyDiscount(); }

        System.out.println("Total Amount: \u20B9" + total);
    }

    public synchronized void checkout() {
        if(itemCount == 0){
            System.out.println("Cart empty. Add items before checkout.");
            return;
        }

        System.out.println("\nProcessing your checkout...\n");

        Thread receiptThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double total = 0;
                try {
                    if(p1 != null){ p1.display(); total += p1.applyDiscount(); Thread.sleep(500); }
                    if(p2 != null){ p2.display(); total += p2.applyDiscount(); Thread.sleep(500); }
                    if(p3 != null){ p3.display(); total += p3.applyDiscount(); Thread.sleep(500); }
                    if(p4 != null){ p4.display(); total += p4.applyDiscount(); Thread.sleep(500); }
                    if(p5 != null){ p5.display(); total += p5.applyDiscount(); Thread.sleep(500); }

                    System.out.println("\nFinal Amount: \u20B9" + total);
                    System.out.println("Thank you for shopping with Mini Market!");
                } catch (InterruptedException e) {
                    System.out.println("Checkout interrupted.");
                }
            }
        });

        receiptThread.start();

        try {
            receiptThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }
    }
}

// Main class
public class MiniCartManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cart cart = new Cart();

        // Predefined product list
        Product[] gadgets = {
            new Product("USB Flash Drive", 500),
            new Product("Wireless Mouse", 800),
            new Product("Bluetooth Headphones", 1500)
        };

        Product[] stationery = {
            new Product("Notebook", 100),
            new Product("Gel Pen Set", 150),
            new Product("Desk Organizer", 250)
        };

        Product[] specials = {
            new SpecialProduct("Portable Charger", 1200, 10),
            new SpecialProduct("Smart LED Lamp", 2000, 15)
        };

        while(true){
            System.out.println("\n===== Mini Market =====");
            System.out.println("1. Add an Item to your Cart");
            System.out.println("2. View Current Cart");
            System.out.println("3. Proceed to Checkout");
            System.out.println("4. Exit Mini Market");
            System.out.print("Choose your option: ");

            String input = sc.nextLine();

            if(input.equals("1")){
                System.out.println("\nSelect Category:");
                System.out.println("1. Tech Gadgets");
                System.out.println("2. Stationery");
                System.out.println("3. Special Deals");
                System.out.print("Enter choice: ");
                int cat = Integer.parseInt(sc.nextLine());

                Product[] selectedCategory = null;

                if(cat == 1) selectedCategory = gadgets;
                else if(cat == 2) selectedCategory = stationery;
                else if(cat == 3) selectedCategory = specials;
                else {
                    System.out.println("Invalid category.");
                    continue;
                }

                System.out.println("\nAvailable Products:");
                for(int i=0; i<selectedCategory.length; i++){
                    System.out.print((i+1) + ". ");
                    selectedCategory[i].display();
                }

                System.out.print("Enter product number to add: ");
                int choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > selectedCategory.length){
                    System.out.println("Invalid product number.");
                    continue;
                }

                cart.addProduct(selectedCategory[choice - 1]);

            }
            else if(input.equals("2")){
                cart.viewCart();
            }
            else if(input.equals("3")){
                cart.checkout();
            }
            else if(input.equals("4")){
                System.out.println("Thank you for using Mini Market!");
                break;
            }
            else{
                System.out.println("Invalid option, try again.");
            }
        }

        sc.close();
    }
}
