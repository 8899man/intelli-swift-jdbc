package com.fr.swift.api.rpc.invoke;

import com.fr.swift.rpc.bean.RpcRequest;
import com.fr.swift.rpc.bean.RpcResponse;

/**
 * @author yee
 * @date 2018/8/26
 */
public interface RpcSender {
    RpcResponse send(RpcRequest request, String address) throws Exception;
}
