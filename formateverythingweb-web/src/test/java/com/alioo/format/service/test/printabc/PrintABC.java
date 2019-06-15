package com.alioo.format.service.test.printabc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintABC {

    boolean printA = true;
    boolean printB = false;
    boolean printC = false;

    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();

        new PrintThread("A", printABC).start();
        new PrintThread("B", printABC).start();
        new PrintThread("C", printABC).start();

    }
}

class PrintThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(PrintABC.class);

    private String str;
    private PrintABC printABC;

    public PrintThread(String str, PrintABC printABC) {
        this.str = str;
        this.printABC = printABC;
    }


    @Override
    public void run() {

        for (int i = 0; i < 1000; i++) {
            synchronized (printABC) {
                if (str.equals("A") ) {
                    while(!printABC.printA) {
                        try {
                            printABC.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                    logger.info(str);

                    printABC.printA = false;
                    printABC.printB = true;
                    printABC.printC = false;

                    printABC.notifyAll();
                } else if (str.equals("B") ) {
                    while(!printABC.printB) {
                        try {
                            printABC.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                    logger.info(str);

                    printABC.printA = false;
                    printABC.printB = false;
                    printABC.printC = true;

                    printABC.notifyAll();

                } else  if (str.equals("C") ) {
                    while(!printABC.printC) {
                        try {
                            printABC.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    logger.info(str);

                    printABC.printA = true;
                    printABC.printB = false;
                    printABC.printC = false;

                    printABC.notifyAll();

                }


            }
        }

    }
}