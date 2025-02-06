package org.fisco.bcos.sdk.demo.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.CallCallback;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class ERC20 extends Contract {
    public static final String[] BINARY_ARRAY = {
        "60806040523480156200001157600080fd5b5060405162000a2138038062000a218339810160408190526200003491620001db565b81516200004990600390602085019062000068565b5080516200005f90600490602084019062000068565b50505062000282565b828054620000769062000245565b90600052602060002090601f0160209004810192826200009a5760008555620000e5565b82601f10620000b557805160ff1916838001178555620000e5565b82800160010185558215620000e5579182015b82811115620000e5578251825591602001919060010190620000c8565b50620000f3929150620000f7565b5090565b5b80821115620000f35760008155600101620000f8565b634e487b7160e01b600052604160045260246000fd5b600082601f8301126200013657600080fd5b81516001600160401b03808211156200015357620001536200010e565b604051601f8301601f19908116603f011681019082821181831017156200017e576200017e6200010e565b816040528381526020925086838588010111156200019b57600080fd5b600091505b83821015620001bf5785820183015181830184015290820190620001a0565b83821115620001d15760008385830101525b9695505050505050565b60008060408385031215620001ef57600080fd5b82516001600160401b03808211156200020757600080fd5b620002158683870162000124565b935060208501519150808211156200022c57600080fd5b506200023b8582860162000124565b9150509250929050565b600181811c908216806200025a57607f821691505b602082108114156200027c57634e487b7160e01b600052602260045260246000fd5b50919050565b61078f80620002926000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c806340c10f191161006657806340c10f191461011857806370a082311461012d57806395d89b4114610156578063a9059cbb1461015e578063dd62ed3e1461017157600080fd5b806306fdde03146100a3578063095ea7b3146100c157806318160ddd146100e457806323b872dd146100f6578063313ce56714610109575b600080fd5b6100ab6101aa565b6040516100b891906105cc565b60405180910390f35b6100d46100cf36600461063d565b61023c565b60405190151581526020016100b8565b6002545b6040519081526020016100b8565b6100d4610104366004610667565b610254565b604051601281526020016100b8565b61012b61012636600461063d565b610278565b005b6100e861013b3660046106a3565b6001600160a01b031660009081526020819052604090205490565b6100ab610286565b6100d461016c36600461063d565b610295565b6100e861017f3660046106c5565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b6060600380546101b9906106f8565b80601f01602080910402602001604051908101604052809291908181526020018280546101e5906106f8565b80156102325780601f1061020757610100808354040283529160200191610232565b820191906000526020600020905b81548152906001019060200180831161021557829003601f168201915b5050505050905090565b60003361024a8185856102a3565b5060019392505050565b6000336102628582856102b5565b61026d858585610338565b506001949350505050565b6102828282610397565b5050565b6060600480546101b9906106f8565b60003361024a818585610338565b6102b083838360016103cd565b505050565b6001600160a01b038381166000908152600160209081526040808320938616835292905220546000198114610332578181101561032357604051637dc7a0d960e11b81526001600160a01b038416600482015260248101829052604481018390526064015b60405180910390fd5b610332848484840360006103cd565b50505050565b6001600160a01b03831661036257604051634b637e8f60e11b81526000600482015260240161031a565b6001600160a01b03821661038c5760405163ec442f0560e01b81526000600482015260240161031a565b6102b08383836104a2565b6001600160a01b0382166103c15760405163ec442f0560e01b81526000600482015260240161031a565b610282600083836104a2565b6001600160a01b0384166103f75760405163e602df0560e01b81526000600482015260240161031a565b6001600160a01b03831661042157604051634a1406b160e11b81526000600482015260240161031a565b6001600160a01b038085166000908152600160209081526040808320938716835292905220829055801561033257826001600160a01b0316846001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9258460405161049491815260200190565b60405180910390a350505050565b6001600160a01b0383166104cd5780600260008282546104c29190610733565b9091555061053f9050565b6001600160a01b038316600090815260208190526040902054818110156105205760405163391434e360e21b81526001600160a01b0385166004820152602481018290526044810183905260640161031a565b6001600160a01b03841660009081526020819052604090209082900390555b6001600160a01b03821661055b5760028054829003905561057a565b6001600160a01b03821660009081526020819052604090208054820190555b816001600160a01b0316836001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516105bf91815260200190565b60405180910390a3505050565b600060208083528351808285015260005b818110156105f9578581018301518582016040015282016105dd565b8181111561060b576000604083870101525b50601f01601f1916929092016040019392505050565b80356001600160a01b038116811461063857600080fd5b919050565b6000806040838503121561065057600080fd5b61065983610621565b946020939093013593505050565b60008060006060848603121561067c57600080fd5b61068584610621565b925061069360208501610621565b9150604084013590509250925092565b6000602082840312156106b557600080fd5b6106be82610621565b9392505050565b600080604083850312156106d857600080fd5b6106e183610621565b91506106ef60208401610621565b90509250929050565b600181811c9082168061070c57607f821691505b6020821081141561072d57634e487b7160e01b600052602260045260246000fd5b50919050565b6000821982111561075457634e487b7160e01b600052601160045260246000fd5b50019056fea264697066735822122074756f9594713f01adb64c8b63fa2006278bb7c4d7e8b009f3fa1a48d3cdad1764736f6c634300080b0033"
    };

    public static final String BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {
        "60806040523480156200001157600080fd5b5060405162000a1d38038062000a1d8339810160408190526200003491620001db565b81516200004990600390602085019062000068565b5080516200005f90600490602084019062000068565b50505062000282565b828054620000769062000245565b90600052602060002090601f0160209004810192826200009a5760008555620000e5565b82601f10620000b557805160ff1916838001178555620000e5565b82800160010185558215620000e5579182015b82811115620000e5578251825591602001919060010190620000c8565b50620000f3929150620000f7565b5090565b5b80821115620000f35760008155600101620000f8565b63b95aa35560e01b600052604160045260246000fd5b600082601f8301126200013657600080fd5b81516001600160401b03808211156200015357620001536200010e565b604051601f8301601f19908116603f011681019082821181831017156200017e576200017e6200010e565b816040528381526020925086838588010111156200019b57600080fd5b600091505b83821015620001bf5785820183015181830184015290820190620001a0565b83821115620001d15760008385830101525b9695505050505050565b60008060408385031215620001ef57600080fd5b82516001600160401b03808211156200020757600080fd5b620002158683870162000124565b935060208501519150808211156200022c57600080fd5b506200023b8582860162000124565b9150509250929050565b600181811c908216806200025a57607f821691505b602082108114156200027c5763b95aa35560e01b600052602260045260246000fd5b50919050565b61078b80620002926000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80636904e965116100665780636904e96514610116578063852d921314610129578063ad8a973114610162578063b11b688314610175578063cc8be70e1461017d57600080fd5b80630256e278146100a35780631f2d4860146100ba57806346b13615146100dd5780634f5e00c7146100ec5780635bfa279614610101575b600080fd5b6002545b6040519081526020015b60405180910390f35b6100cd6100c83660046105e4565b6101a6565b60405190151581526020016100b1565b604051601281526020016100b1565b6100ff6100fa3660046105e4565b6101be565b005b6101096101cc565b6040516100b1919061060e565b6100cd6101243660046105e4565b61025e565b6100a7610137366004610663565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b6100cd610170366004610696565b61026c565b610109610290565b6100a761018b3660046106d2565b6001600160a01b031660009081526020819052604090205490565b6000336101b481858561029f565b5060019392505050565b6101c882826102b1565b5050565b6060600480546101db906106f4565b80601f0160208091040260200160405190810160405280929190818152602001828054610207906106f4565b80156102545780601f1061022957610100808354040283529160200191610254565b820191906000526020600020905b81548152906001019060200180831161023757829003601f168201915b5050505050905090565b6000336101b48185856102ec565b60003361027a85828561034b565b6102858585856102ec565b506001949350505050565b6060600380546101db906106f4565b6102ac83838360016103c9565b505050565b6001600160a01b0382166102e0576040516360af35d960e11b8152600060048201526024015b60405180910390fd5b6101c86000838361049e565b6001600160a01b038316610316576040516388e4d34560e01b8152600060048201526024016102d7565b6001600160a01b038216610340576040516360af35d960e11b8152600060048201526024016102d7565b6102ac83838361049e565b6001600160a01b0383811660009081526001602090815260408083209386168352929052205460001981146103c357818110156103b4576040516362b40b1d60e01b81526001600160a01b038416600482015260248101829052604481018390526064016102d7565b6103c3848484840360006103c9565b50505050565b6001600160a01b0384166103f357604051635297c25960e11b8152600060048201526024016102d7565b6001600160a01b03831661041d5760405163513e5c5560e11b8152600060048201526024016102d7565b6001600160a01b03808516600090815260016020908152604080832093871683529290522082905580156103c357826001600160a01b0316846001600160a01b03167fd1e45707b3f71c77903b61f04c900f772db264b9bf618f1cc3308fb516eb61698460405161049091815260200190565b60405180910390a350505050565b6001600160a01b0383166104c95780600260008282546104be919061072f565b9091555061053b9050565b6001600160a01b0383166000908152602081905260409020548181101561051c57604051631c40795b60e31b81526001600160a01b038516600482015260248101829052604481018390526064016102d7565b6001600160a01b03841660009081526020819052604090209082900390555b6001600160a01b03821661055757600280548290039055610576565b6001600160a01b03821660009081526020819052604090208054820190555b816001600160a01b0316836001600160a01b03167f18f84334255a242551aa98c68047b5da8063eab9fbeaec1eddeea280044b9ff1836040516105bb91815260200190565b60405180910390a3505050565b80356001600160a01b03811681146105df57600080fd5b919050565b600080604083850312156105f757600080fd5b610600836105c8565b946020939093013593505050565b600060208083528351808285015260005b8181101561063b5785810183015185820160400152820161061f565b8181111561064d576000604083870101525b50601f01601f1916929092016040019392505050565b6000806040838503121561067657600080fd5b61067f836105c8565b915061068d602084016105c8565b90509250929050565b6000806000606084860312156106ab57600080fd5b6106b4846105c8565b92506106c2602085016105c8565b9150604084013590509250925092565b6000602082840312156106e457600080fd5b6106ed826105c8565b9392505050565b600181811c9082168061070857607f821691505b602082108114156107295763b95aa35560e01b600052602260045260246000fd5b50919050565b600082198211156107505763b95aa35560e01b600052601160045260246000fd5b50019056fea2646970667358221220d6bcdad20cd00263e23b1ca6bdcf4e7ef3cee43d164b611a9f9884f45c52b90f64736f6c634300080b0033"
    };

    public static final String SM_BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"name_\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"symbol_\",\"type\":\"string\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"spender\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"allowance\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"needed\",\"type\":\"uint256\"}],\"name\":\"ERC20InsufficientAllowance\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"sender\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"balance\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"needed\",\"type\":\"uint256\"}],\"name\":\"ERC20InsufficientBalance\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"approver\",\"type\":\"address\"}],\"name\":\"ERC20InvalidApprover\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"receiver\",\"type\":\"address\"}],\"name\":\"ERC20InvalidReceiver\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"sender\",\"type\":\"address\"}],\"name\":\"ERC20InvalidSender\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"spender\",\"type\":\"address\"}],\"name\":\"ERC20InvalidSpender\",\"type\":\"error\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"spender\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"Approval\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"spender\",\"type\":\"address\"}],\"name\":\"allowance\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"spender\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approve\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"decimals\",\"outputs\":[{\"internalType\":\"uint8\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"mint\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"totalSupply\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transfer\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferFrom\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"
    };

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final Event APPROVAL_EVENT =
            new Event(
                    "Approval",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Address>(true) {},
                            new TypeReference<Address>(true) {},
                            new TypeReference<Uint256>() {}));;

    public static final Event TRANSFER_EVENT =
            new Event(
                    "Transfer",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Address>(true) {},
                            new TypeReference<Address>(true) {},
                            new TypeReference<Uint256>() {}));;

    protected ERC20(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses =
                new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeApprovalEvent(
            BigInteger fromBlock,
            BigInteger toBlock,
            List<String> otherTopics,
            EventSubCallback callback) {
        String topic0 = eventEncoder.encode(APPROVAL_EVENT);
        subscribeEvent(topic0, otherTopics, fromBlock, toBlock, callback);
    }

    public void subscribeApprovalEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(APPROVAL_EVENT);
        subscribeEvent(topic0, callback);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses =
                new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTransferEvent(
            BigInteger fromBlock,
            BigInteger toBlock,
            List<String> otherTopics,
            EventSubCallback callback) {
        String topic0 = eventEncoder.encode(TRANSFER_EVENT);
        subscribeEvent(topic0, otherTopics, fromBlock, toBlock, callback);
    }

    public void subscribeTransferEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(TRANSFER_EVENT);
        subscribeEvent(topic0, callback);
    }

    public BigInteger allowance(String owner, String spender) throws ContractException {
        final Function function =
                new Function(
                        FUNC_ALLOWANCE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(owner),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodAllowanceRawFunction(String owner, String spender)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_ALLOWANCE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(owner),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return function;
    }

    public void allowance(String owner, String spender, CallCallback callback)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_ALLOWANCE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(owner),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        asyncExecuteCall(function, callback);
    }

    public TransactionReceipt approve(String spender, BigInteger value) {
        final Function function =
                new Function(
                        FUNC_APPROVE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public Function getMethodApproveRawFunction(String spender, BigInteger value)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_APPROVE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public String getSignedTransactionForApprove(String spender, BigInteger value) {
        final Function function =
                new Function(
                        FUNC_APPROVE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public String approve(String spender, BigInteger value, TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_APPROVE,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(spender),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<String, BigInteger> getApproveInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_APPROVE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(
                (String) results.get(0).getValue(), (BigInteger) results.get(1).getValue());
    }

    public Tuple1<Boolean> getApproveOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_APPROVE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public BigInteger balanceOf(String account) throws ContractException {
        final Function function =
                new Function(
                        FUNC_BALANCEOF,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(account)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodBalanceOfRawFunction(String account) throws ContractException {
        final Function function =
                new Function(
                        FUNC_BALANCEOF,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(account)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return function;
    }

    public void balanceOf(String account, CallCallback callback) throws ContractException {
        final Function function =
                new Function(
                        FUNC_BALANCEOF,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(account)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        asyncExecuteCall(function, callback);
    }

    public BigInteger decimals() throws ContractException {
        final Function function =
                new Function(
                        FUNC_DECIMALS,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodDecimalsRawFunction() throws ContractException {
        final Function function =
                new Function(
                        FUNC_DECIMALS,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return function;
    }

    public void decimals(CallCallback callback) throws ContractException {
        final Function function =
                new Function(
                        FUNC_DECIMALS,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        asyncExecuteCall(function, callback);
    }

    public TransactionReceipt mint(String to, BigInteger amount) {
        final Function function =
                new Function(
                        FUNC_MINT,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(
                                        amount)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public Function getMethodMintRawFunction(String to, BigInteger amount)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_MINT,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(
                                        amount)),
                        Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForMint(String to, BigInteger amount) {
        final Function function =
                new Function(
                        FUNC_MINT,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(
                                        amount)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public String mint(String to, BigInteger amount, TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_MINT,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(
                                        amount)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<String, BigInteger> getMintInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_MINT,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(
                (String) results.get(0).getValue(), (BigInteger) results.get(1).getValue());
    }

    public String name() throws ContractException {
        final Function function =
                new Function(
                        FUNC_NAME,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodNameRawFunction() throws ContractException {
        final Function function =
                new Function(
                        FUNC_NAME,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return function;
    }

    public void name(CallCallback callback) throws ContractException {
        final Function function =
                new Function(
                        FUNC_NAME,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        asyncExecuteCall(function, callback);
    }

    public String symbol() throws ContractException {
        final Function function =
                new Function(
                        FUNC_SYMBOL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodSymbolRawFunction() throws ContractException {
        final Function function =
                new Function(
                        FUNC_SYMBOL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return function;
    }

    public void symbol(CallCallback callback) throws ContractException {
        final Function function =
                new Function(
                        FUNC_SYMBOL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        asyncExecuteCall(function, callback);
    }

    public BigInteger totalSupply() throws ContractException {
        final Function function =
                new Function(
                        FUNC_TOTALSUPPLY,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodTotalSupplyRawFunction() throws ContractException {
        final Function function =
                new Function(
                        FUNC_TOTALSUPPLY,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return function;
    }

    public void totalSupply(CallCallback callback) throws ContractException {
        final Function function =
                new Function(
                        FUNC_TOTALSUPPLY,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        asyncExecuteCall(function, callback);
    }

    public TransactionReceipt transfer(String to, BigInteger value) {
        final Function function =
                new Function(
                        FUNC_TRANSFER,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public Function getMethodTransferRawFunction(String to, BigInteger value)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_TRANSFER,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public String getSignedTransactionForTransfer(String to, BigInteger value) {
        final Function function =
                new Function(
                        FUNC_TRANSFER,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public String transfer(String to, BigInteger value, TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_TRANSFER,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<String, BigInteger> getTransferInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_TRANSFER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(
                (String) results.get(0).getValue(), (BigInteger) results.get(1).getValue());
    }

    public Tuple1<Boolean> getTransferOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_TRANSFER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt transferFrom(String from, String to, BigInteger value) {
        final Function function =
                new Function(
                        FUNC_TRANSFERFROM,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(from),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public Function getMethodTransferFromRawFunction(String from, String to, BigInteger value)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_TRANSFERFROM,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(from),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public String getSignedTransactionForTransferFrom(String from, String to, BigInteger value) {
        final Function function =
                new Function(
                        FUNC_TRANSFERFROM,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(from),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public String transferFrom(
            String from, String to, BigInteger value, TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_TRANSFERFROM,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(from),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(value)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple3<String, String, BigInteger> getTransferFromInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_TRANSFERFROM,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Address>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint256>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, BigInteger>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue());
    }

    public Tuple1<Boolean> getTransferFromOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_TRANSFERFROM,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public static ERC20 load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new ERC20(contractAddress, client, credential);
    }

    public static ERC20 deploy(
            Client client, CryptoKeyPair credential, String name_, String symbol_)
            throws ContractException {
        byte[] encodedConstructor =
                FunctionEncoder.encodeConstructor(
                        Arrays.<Type>asList(
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(name_),
                                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(symbol_)));
        return deploy(
                ERC20.class,
                client,
                credential,
                getBinary(client.getCryptoSuite()),
                getABI(),
                encodedConstructor,
                null);
    }

    public static class ApprovalEventResponse {
        public TransactionReceipt.Logs log;

        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse {
        public TransactionReceipt.Logs log;

        public String from;

        public String to;

        public BigInteger value;
    }
}
