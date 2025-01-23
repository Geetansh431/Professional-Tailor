package org.example.project.measurementExplorer;

public class Order {
    private int orderId;
    private String mobileNumber;
    private String dress;
    private String pic;
    private String dodelDate;
    private int quantity;
    private int bill;
    private String worker;

    public Order(int orderId, String mobileNumber, String dress, String pic, String dodelDate, int quantity, int bill, String worker) {
        this.orderId = orderId;
        this.mobileNumber = mobileNumber;
        this.dress = dress;
        this.pic = pic;
        this.dodelDate = dodelDate;
        this.quantity = quantity;
        this.bill = bill;
        this.worker = worker;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDress() {
        return dress;
    }

    public String getPic() {
        return pic;
    }

    public String getDodelDate() {
        return dodelDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBill() {
        return bill;
    }

    public String getWorker() {
        return worker;
    }
}
