/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.RequestException;
import br.uff.labtempo.omcp.common.exceptions.UnreachableModuleException;

/**
 *
 * @author Felipe
 */
public interface OmcpClient extends AutoCloseable {

    void setSourceId(String id);

    Response doGet(String url) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPost(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPut(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doDelete(String url) throws UnreachableModuleException, RequestException, BadResponseException;

    void doNofity(String url, String content) throws UnreachableModuleException, RequestException;
}
