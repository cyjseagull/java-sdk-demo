/**
 * Copyright 2014-2020 [fisco-dev]
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fisco.bcos.sdk.demo.perf;

import com.google.common.util.concurrent.RateLimiter;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.fisco.bcos.sdk.demo.contract.ERC20;
import org.fisco.bcos.sdk.demo.perf.callback.PerformanceCallback;
import org.fisco.bcos.sdk.demo.perf.collector.PerformanceCollector;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.BcosSDKException;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.ConstantConfig;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;
import org.fisco.bcos.sdk.v3.utils.ThreadPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceERC20 {
    private static Logger logger = LoggerFactory.getLogger(PerformanceERC20.class);
    private static AtomicInteger sendedTransactions = new AtomicInteger(0);

    private static void Usage() {
        System.out.println(" Usage:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' org.fisco.bcos.sdk.demo.perf.PerformanceERC20 [user_count] [total] [tps] [groupId].");
    }

    public static void main(String[] args) {
        try {
            String configFileName = ConstantConfig.CONFIG_FILE_NAME;
            URL configUrl = PerformanceOk.class.getClassLoader().getResource(configFileName);

            if (configUrl == null) {
                System.out.println("The configFile " + configFileName + " doesn't exist!");
                return;
            }
            if (args.length < 3) {
                Usage();
                return;
            }
            Integer userCount = Integer.valueOf(args[0]);
            final Integer total = Integer.valueOf(args[1]);
            Integer qps = Integer.valueOf(args[2]);
            String groupId = args[3];
            System.out.println(
                    "====== PerformanceERC20 perf, total: "
                            + total
                            + ", qps:"
                            + qps
                            + ", user count: "
                            + userCount
                            + ", groupId: "
                            + groupId);

            String configFile = configUrl.getPath();
            BcosSDK sdk = BcosSDK.build(configFile);

            // build the client
            Client client = sdk.getClient(groupId);

            // deploy the erc20
            System.out.println("====== Deploy Ok ====== ");
            CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
            ERC20 erc20 = ERC20.deploy(client, cryptoKeyPair, "erc20", "erc20Symbol");
            System.out.println(
                    "====== Deploy ERC20 success, address: "
                            + erc20.getContractAddress()
                            + " ====== ");
            /// mint
            BigInteger mintMount = BigInteger.valueOf((long) 2 * total);
            TransactionReceipt receipt = erc20.mint(cryptoKeyPair.getAddress(), mintMount);
            if (receipt.isStatusOK()) {
                System.out.println(
                        "==== Mint erc20 success, to: "
                                + cryptoKeyPair.getAddress()
                                + ", amount: "
                                + mintMount);
            } else {
                System.out.println(
                        "=== Mint erc20 failed, to: "
                                + cryptoKeyPair.getAddress()
                                + ", amount: "
                                + mintMount
                                + ", status: "
                                + receipt.getStatus());
                System.exit(-1);
            }
            System.out.println("==== Generate user account, num: " + userCount + " ====");
            List<String> targetUsers = new ArrayList<>();
            for (Integer i = 0; i < userCount; i++) {
                CryptoKeyPair accountKeyPair = client.getCryptoSuite().generateRandomKeyPair();
                targetUsers.add(accountKeyPair.getAddress());
            }
            System.out.println("==== Generate user account success, num: " + userCount + " ====");

            PerformanceCollector collector = new PerformanceCollector();
            collector.setTotal(total);
            RateLimiter limiter = RateLimiter.create(qps);
            Integer area = total / 10;
            System.out.println("====== PerformanceOk trans start, total" + total + "======");
            ThreadPoolService threadPoolService =
                    new ThreadPoolService("PerformanceERC20", 1000000);
            for (Integer i = 0; i < total; ++i) {
                limiter.acquire();
                threadPoolService
                        .getThreadPool()
                        .execute(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        PerformanceCallback callback = new PerformanceCallback();
                                        callback.setTimeout(0);
                                        callback.setCollector(collector);
                                        try {
                                            String toAccount = targetUsers.get(total % userCount);
                                            erc20.transfer(
                                                    toAccount, BigInteger.valueOf(1), callback);
                                        } catch (Exception e) {
                                            TransactionReceipt receipt = new TransactionReceipt();
                                            receipt.setStatus(-1);
                                            callback.onResponse(receipt);
                                            logger.info(e.getMessage());
                                        }
                                        int current = sendedTransactions.incrementAndGet();
                                        if (current >= area && ((current % area) == 0)) {
                                            System.out.println(
                                                    "Already sended: "
                                                            + current
                                                            + "/"
                                                            + total
                                                            + " transactions");
                                        }
                                    }
                                });
            }
            // wait to collect all the receipts
            while (!collector.getReceived().equals(total)) {
                Thread.sleep(1000);
            }
            threadPoolService.stop();
            System.exit(0);
        } catch (BcosSDKException | ContractException | InterruptedException e) {
            System.out.println(
                    "====== PerformanceOk test failed, error message: " + e.getMessage());
            System.exit(0);
        }
    }
}
