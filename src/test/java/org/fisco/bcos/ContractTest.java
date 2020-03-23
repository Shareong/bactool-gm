package org.fisco.bcos;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.temp.HelloWorld;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractTest extends BaseTest {

    @Autowired private Web3j web3j;
    @Autowired private Credentials credentials;

    @Test
    public void deployAndCallHelloWorld() throws Exception {
        // deploy contract
        HelloWorld helloWorld =
                HelloWorld.deploy(
                                web3j,
                                credentials,
                                new StaticGasProvider(
                                        GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                        .send();
        if (helloWorld != null) {
            System.out.println("HelloWorld address is: " + helloWorld.getContractAddress());
            // call set function
            helloWorld.set("Hello, World!").send();
            // call get function
            String result = helloWorld.get().send();
            System.out.println(result);
            assertTrue("Hello, World!".equals(result));
        }
    }

    @Test
    public void testBAC001() throws Exception {

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("2100000000");
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        String Sender = "0x55f934bcbe1e9aef8337f5551142a442fdde781c";
        String Receiver = "0x2b5ad5c4795c026514f8317c7a215e218dccd6cf";
        String Contract = "0x82df1ce5de28636213492128b3381c3000f53c10";

        System.out.println("Sender: " + credentials.getAddress());

        // 发行bac001
        BAC001 bac001 =
                BAC001.deploy(
                                web3j,
                                credentials,
                                contractGasProvider,
                                "htlc ledger",
                                "HTLCOIN",
                                BigInteger.valueOf(1),
                                BigInteger.valueOf(100000000))
                        .send();

        String bacContractAddress = bac001.getContractAddress();

        System.out.println("bac address: " + bacContractAddress);
        System.out.println("Sender withdraw: " + bac001.balance(Sender).send());

        // 批准合约可以转走资产
        bac001.approve(Contract, new BigInteger("100000000")).send();

        System.out.println("Sender withdraw: " + bac001.balance(Sender).send());
        System.out.println("Receiver withdraw: " + bac001.balance(Receiver).send());
    }

    @Test
    public void ledgerPrint() throws Exception {

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("2100000000");
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        String Sender = "0x55f934bcbe1e9aef8337f5551142a442fdde781c";
        String Receiver = "0x2b5ad5c4795c026514f8317c7a215e218dccd6cf";
        String HTLCContract = "0x82df1ce5de28636213492128b3381c3000f53c10";

        System.out.println("Sender: " + credentials.getAddress());

        BAC001 bac001 =
                BAC001.load(
                        "0x9925ab7b9a0e17361da8fc0caa6a03b4623c6385",
                        web3j,
                        credentials,
                        contractGasProvider);

        System.out.println("Sender withdraw: " + bac001.balance(Sender).send());
        System.out.println("Receiver withdraw: " + bac001.balance(Receiver).send());
        System.out.println("HTLCContract withdraw: " + bac001.balance(HTLCContract).send());
    }
}
