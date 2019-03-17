package se.bth.rentalSystem_server.models;

public enum Category {
    Electronics_Computers(0),
    Home_Garden(1),
    Sports_Outdoors(2),
    Health_Beauty(3),
    Car_Motorbike(4),
    Home_Services(5),
    Prime_Video(6),
    Books(7),
    Handmade(8),
    Personal(9),
    Others(10);

    private final int index;

    Category(int index) {
        this.index = index;
    }
}
