package org.fisco.bcos.bac;

import java.math.BigInteger;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bac {
    private Credentials credentials;
    private Web3j web3j;
    private String assetAddress;

    public Bac() {
        try {
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            Service service = context.getBean(Service.class);
            service.run();

            ChannelEthereumService channelService = new ChannelEthereumService();
            channelService.setChannelService(service);
            channelService.setTimeout(5000);
            web3j = Web3j.build(channelService, service.getGroupId());

            AccountConfig accountConfig = context.getBean(AccountConfig.class);
            credentials = accountConfig.getCredentials();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initAssetContract(String htlcAddress) {
        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("2100000000");
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
        try {
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
            assetAddress = bac001.getContractAddress();
            bac001.approve(htlcAddress, new BigInteger("100000000")).send();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("assetAddress: " + assetAddress);
        System.out.println("owner: " + credentials.getAddress());

        System.exit(0);
    }

    public void getBalance(String assetAddress, String address) {
        try {
            BigInteger gasPrice = new BigInteger("1");
            BigInteger gasLimit = new BigInteger("2100000000");
            ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
            BAC001 bac001 = BAC001.load(assetAddress, web3j, credentials, contractGasProvider);
            System.out.println("balance: " + bac001.balance(address).send());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
