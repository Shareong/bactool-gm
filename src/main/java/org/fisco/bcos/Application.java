package org.fisco.bcos;

import org.fisco.bcos.bac.Bac;

public class Application {
    public static void main(String[] args) {
        String method = args[0];
        Bac bac = new Bac();
        if (method.equalsIgnoreCase("init")) {
            bac.initAssetContract(args[1]);
        } else if (method.equalsIgnoreCase("getBalance")) {
            bac.getBalance(args[1], args[2]);
        } else {
            System.out.println("method not found");
        }
    }
}
