import java.util.concurrent.locks.*;

class TicketBookingSystem {
    private static final int TOTAL_SEATS = 10;
    private boolean[] seats = new boolean[TOTAL_SEATS];
    private final Lock lock = new ReentrantLock();

    public void bookSeat(int seatNumber, String passenger) {
        lock.lock();
        try {
            if (seatNumber < 0 || seatNumber >= TOTAL_SEATS) {
                System.out.println(passenger + " attempted to book an invalid seat.");
                return;
            }
            if (!seats[seatNumber]) {
                seats[seatNumber] = true;
                System.out.println(passenger + " successfully booked seat " + seatNumber);
            } else {
                System.out.println(passenger + " attempted to book an already booked seat " + seatNumber);
            }
        } finally {
            lock.unlock();
        }
    }
}

class Passenger extends Thread {
    private TicketBookingSystem system;
    private int seatNumber;
    private String name;

    public Passenger(TicketBookingSystem system, int seatNumber, String name, int priority) {
        this.system = system;
        this.seatNumber = seatNumber;
        this.name = name;
        this.setPriority(priority);
    }

    @Override
    public void run() {
        system.bookSeat(seatNumber, name);
    }
}

public class TicketBookingApp {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem();
        
        Thread vip1 = new Passenger(system, 2, "VIP-1", Thread.MAX_PRIORITY);
        Thread vip2 = new Passenger(system, 3, "VIP-2", Thread.MAX_PRIORITY);
        Thread normal1 = new Passenger(system, 2, "Normal-1", Thread.NORM_PRIORITY);
        Thread normal2 = new Passenger(system, 5, "Normal-2", Thread.NORM_PRIORITY);
        Thread normal3 = new Passenger(system, 3, "Normal-3", Thread.NORM_PRIORITY);

        vip1.start();
        vip2.start();
        normal1.start();
        normal2.start();
        normal3.start();
    }
}
