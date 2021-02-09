package kz.edu.astanait;

import kz.edu.astanait.model.Payment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentLogger extends Thread{
    private File file;
    private Payment payment;

    public PaymentLogger(Payment payment){
        file = new File("C:\\Users\\oians\\IdeaProjects\\AJ2-Assignment-3\\src\\main\\java\\kz\\edu\\astanait\\logs\\mylogfile.log");
        this.payment = payment;
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String logMsg = '[' + dateFormat.format(currentDate)
                + " " + payment.getId()
                + " " + payment.getPaymentAmount()
                + " " + payment.getPaymentDate()
                + " " + payment.getPaymentDestination()
                + "]";

        LogWriter.writeLog(file, logMsg);
    }
}
