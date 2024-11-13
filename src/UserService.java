import java.util.ArrayList;
import java.util.List;


interface IUserService {
    User register(String username, String password);
    User login(String username, String password);
}

 interface IProductService {
    List<Product> getProducts();
    Product addProduct(Product product);
}

 interface IOrderService {
    Order createOrder(int userId, List<Integer> productIds);
    Order getOrderStatus(int orderId);
}

interface IPaymentService {
    boolean processPayment(int orderId, double amount);
}

interface INotificationService {
    void sendNotification(int userId, String message);
}


class User {
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// Класс для продукта
class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}


class Order {
    private int id;
    private int userId;
    private List<Product> products;
    private String status;

    public Order(int userId, List<Product> products, String status) {
        this.id = (int) (Math.random() * 1000);
        this.userId = userId;
        this.products = products;
        this.status = status;
    }

    public Order(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


class UserService implements IUserService {
    private List<User> users = new ArrayList<>();

    @Override
    public User register(String username, String password) {
        int id = users.size() + 1;
        User user = new User(id, username, password);
        users.add(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        return null;
    }
}


class ProductService implements IProductService {
    private List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public Product addProduct(Product product) {
        products.add(product);
        return product;
    }
}


class PaymentService implements IPaymentService {
    @Override
    public boolean processPayment(int orderId, double amount) {
        return true;
    }
}


class NotificationService implements INotificationService {
    @Override
    public void sendNotification(int userId, String message) {
        System.out.println("Уведомление пользователю " + userId + ": " + message);
    }
}


class OrderService implements IOrderService {
    private final IProductService productService;
    private final IPaymentService paymentService;
    private final INotificationService notificationService;

    public OrderService(IProductService productService, IPaymentService paymentService, INotificationService notificationService) {
        this.productService = productService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    @Override
    public Order createOrder(int userId, List<Integer> productIds) {
        List<Product> products = productService.getProducts();
        products.removeIf(p -> !productIds.contains(p.getId()));
        if (products.isEmpty()) {
            throw new RuntimeException("Выбранные товары не найдены.");
        }

        Order order = new Order(userId, products, "Created");

        double totalAmount = products.stream().mapToDouble(Product::getPrice).sum();
        if (paymentService.processPayment(order.getId(), totalAmount)) {
            order.setStatus("Paid");
            notificationService.sendNotification(userId, "Ваш заказ успешно оплачен.");
        } else {
            order.setStatus("Payment Failed");
            notificationService.sendNotification(userId, "Платеж не прошел. Попробуйте снова.");
        }

        return order;
    }

    @Override
    public Order getOrderStatus(int orderId) {
        return new Order(orderId, "In Progress");
    }
}


class UserServiceApp {
    public static void main(String[] args) {

        IProductService productService = new ProductService();
        IPaymentService paymentService = new PaymentService();
        INotificationService notificationService = new NotificationService();
        IOrderService orderService = new OrderService(productService, paymentService, notificationService);
        IUserService userService = new UserService();


        User user = userService.register("testuser", "password123");


        productService.addProduct(new Product(1, "Acer", 1000.0));
        productService.addProduct(new Product(2, "Samsung s10 plus", 500.0));


        List<Integer> productIds = List.of(1, 2);
        Order order = orderService.createOrder(user.getId(), productIds);

        System.out.println("Статус заказа: " + order.getStatus());
    }
}
