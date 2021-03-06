/*
Copyright © 2019 Salt Edge. https://saltedge.com
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.saltedge.sdk.network;

import android.text.TextUtils;

import com.saltedge.sdk.connector.AccountsConnector;
import com.saltedge.sdk.connector.ConnectSessionConnector;
import com.saltedge.sdk.connector.ConnectionDeleteConnector;
import com.saltedge.sdk.connector.ConnectionsConnector;
import com.saltedge.sdk.connector.ConsentRevokeConnector;
import com.saltedge.sdk.connector.ConsentsConnector;
import com.saltedge.sdk.connector.CustomerConnector;
import com.saltedge.sdk.connector.ProvidersConnector;
import com.saltedge.sdk.connector.TransactionsConnector;
import com.saltedge.sdk.interfaces.ConnectSessionResult;
import com.saltedge.sdk.interfaces.CreateCustomerResult;
import com.saltedge.sdk.interfaces.DeleteEntryResult;
import com.saltedge.sdk.interfaces.FetchAccountsResult;
import com.saltedge.sdk.interfaces.FetchConnectionsResult;
import com.saltedge.sdk.interfaces.FetchConsentsResult;
import com.saltedge.sdk.interfaces.FetchTransactionsResult;
import com.saltedge.sdk.interfaces.ProvidersResult;
import com.saltedge.sdk.interfaces.RefreshConnectionResult;
import com.saltedge.sdk.model.SEConnection;
import com.saltedge.sdk.utils.SEConstants;

import java.util.Map;

/**
 * Helper class for Saltedge SDK requests
 */
public class SERequestManager {

    private static SERequestManager instance;

    public static SERequestManager getInstance() {
        if (instance == null) {
            instance = new SERequestManager();
        }
        return instance;
    }

    /**
     * Return list of available Providers.
     * Result is returned through callback.
     *
     * @param countryCode - code of the provider’s country
     * @param callback - callback for request result
     */
    public void fetchProviders(String countryCode, ProvidersResult callback) {
        new ProvidersConnector(callback).fetchProviders(countryCode);
    }

    /**
     * Allows to create new Customer.
     * Result is returned through callback.
     *
     * @param customerIdentifier - a unique identifier of the new Customer
     * @param callback - callback for request result
     */
    public void createCustomer(String customerIdentifier, CreateCustomerResult callback) {
        if (customerIdentifier == null || TextUtils.isEmpty(customerIdentifier)) {
            throw new RuntimeException(SEConstants.KEY_SECRET.concat(" " + SEConstants.CANNOT_BE_NULL));
        }
        new CustomerConnector(callback).createCustomer(customerIdentifier);
    }

    /**
     * Allows you to create a connect session, which will be used to access Salt Edge Connect for Connection creation.
     * You will receive a connect_url, which allows you to enter directly to Salt Edge Connect with your newly generated connect session.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param providerCode - the code of the desired Provider
     * @param consentScopes - fetching mode, possible values: ['holder_information], ['account_details'], ['transactions_details'] or combinations
     * @param localeCode - the language of the Salt Edge Connect page in the ISO 639-1 format.
     * @param returnToUrl - the URL the user will be redirected to. The return_to URL should not exceed 2040 characters.
     * @param callback - callback for request result
     */
    public void createConnectSession(String customerSecret,
                                     String providerCode,
                                     String[] consentScopes,
                                     String localeCode,
                                     String returnToUrl,
                                     ConnectSessionResult callback) {
        new ConnectSessionConnector(callback).createConnectSession(customerSecret, providerCode, consentScopes, localeCode, returnToUrl);
    }

    /**
     * Allows you to create a connect session, which will be used to access Salt Edge Connect for Connection creation.
     * You will receive a connect_url, which allows you to enter directly to Salt Edge Connect with your newly generated connect session.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param dataMap - custom params map
     * @param callback - callback for request result
     */
    public void createConnectSession(String customerSecret,
                                     Map<String, Object> dataMap,
                                     ConnectSessionResult callback) {
        new ConnectSessionConnector(callback).createConnectSession(customerSecret, dataMap);
    }

    /**
     * Allows you to create a connect session, which will be used to access Salt Edge Connect to reconnect a connection.
     * You will receive a connect_url, which allows you to enter directly to Salt Edge Connect with your newly generated connect session.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret of the Connection which you want to reconnect
     * @param consentScopes - fetching mode, possible values: ['holder_information], ['account_details'], ['transactions_details'] or combinations
     * @param localeCode - the language of the Salt Edge Connect page in the ISO 639-1 format.
     * @param returnToUrl - the URL the user will be redirected to. The return_to URL should not exceed 2040 characters.
     * @param callback - callback for request result
     */
    public void createReconnectSession(String customerSecret,
                                       String connectionSecret,
                                       String[] consentScopes,
                                       String localeCode,
                                       String returnToUrl,
                                       ConnectSessionResult callback) {
        new ConnectSessionConnector(callback).createReconnectSession(
                customerSecret,
                connectionSecret,
                consentScopes,
                localeCode,
                returnToUrl,
                false);
    }

    /**
     * Allows you to create a connect session, which will be used to access Salt Edge Connect to reconnect a connection.
     * You will receive a connect_url, which allows you to enter directly to Salt Edge Connect with your newly generated connect session.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret of the Connection which you want to reconnect
     * @param consentScopes - fetching mode, possible values: ['holder_information], ['account_details'], ['transactions_details'] or combinations
     * @param localeCode - the language of the Salt Edge Connect page in the ISO 639-1 format.
     * @param returnToUrl - the URL the user will be redirected to. The return_to URL should not exceed 2040 characters.
     * @param overrideCredentials - override credentials strategy. If true, the new credentials will automatically override the old ones.
     * @param callback - callback for request result
     */
    public void createReconnectSession(String customerSecret,
                                       String connectionSecret,
                                       String[] consentScopes,
                                       String localeCode,
                                       String returnToUrl,
                                       boolean overrideCredentials,
                                       ConnectSessionResult callback) {
        new ConnectSessionConnector(callback).createReconnectSession(
                customerSecret,
                connectionSecret,
                consentScopes,
                localeCode,
                returnToUrl,
                overrideCredentials);
    }

    /**
     * Allows you to create a connect session, which will be used to access Salt Edge Connect to refresh a connection.
     * You will receive a connect_url, which allows you to enter directly to Salt Edge Connect with your newly generated connect session.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection which you want to reconnect
     * @param localeCode - the language of the Salt Edge Connect page in the ISO 639-1 format.
     * @param returnToUrl - the URL the user will be redirected to. The return_to URL should not exceed 2040 characters.
     * @param callback - callback for request result
     */
    public void createRefreshSession(String customerSecret,
                                     String connectionSecret,
                                     String localeCode,
                                     String returnToUrl,
                                     ConnectSessionResult callback) {
        new ConnectSessionConnector(callback).createRefreshSession(customerSecret, connectionSecret, localeCode, returnToUrl);
    }

    /**
     * Created SERefreshService and starts refresh through Salt Edge API
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionData - Connection data for refresh
     * @param refreshScopes - array of refresh scopes, e.g. {"accounts", "transactions"}
     * @param callback - callback for request result
     * @return SERefreshService - refresh service object
     */
    public SERefreshService refreshConnectionWithSecret(String customerSecret,
                                                        SEConnection connectionData,
                                                        String[] refreshScopes,
                                                        RefreshConnectionResult callback) {
        return new SERefreshService(callback).startRefresh(customerSecret, connectionData, refreshScopes);
    }

    /**
     * Returns a single Connection object.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection which should be returned if exist
     * @param callback - callback for request result
     */
    public void fetchConnection(String customerSecret,
                                String connectionSecret,
                                FetchConnectionsResult callback) {
        String[] secrets = { connectionSecret };
        fetchConnections(customerSecret, secrets, callback);
    }

    /**
     * Returns Connections objects collection.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionsSecretsArray - array of secrets of the Connections which should be returned if exist
     * @param callback - callback for request result
     */
    public void fetchConnections(String customerSecret,
                                 String[] connectionsSecretsArray,
                                 FetchConnectionsResult callback) {
        new ConnectionsConnector(callback).fetchConnections(customerSecret, connectionsSecretsArray);
    }

    /**
     * Delete Connection.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection which should be deleted if exist
     * @param callback - callback for request result
     */
    public void deleteConnection(String customerSecret,
                                 String connectionSecret,
                                 DeleteEntryResult callback) {
        new ConnectionDeleteConnector(callback).deleteConnection(customerSecret, connectionSecret);
    }

    /**
     * You can see the list of accounts of a Connection.
     * The accounts are sorted in ascending order of their ID, so the newest accounts will come last.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection
     * @param callback - callback for request result
     */
    public void fetchAccounts(String customerSecret,
                              String connectionSecret,
                              FetchAccountsResult callback) {
        new AccountsConnector(callback).fetchAccounts(customerSecret, connectionSecret);
    }

    /**
     * Return the list of all non duplicated  transactions for an Account of a Connection.
     * The list not includes pending transactions
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection
     * @param accountId - Account ID
     * @param callback - callback for request result
     */
    public void fetchAllTransactions(String customerSecret,
                                     String connectionSecret,
                                     String accountId,
                                     FetchTransactionsResult callback) {
        fetchTransactions(customerSecret, connectionSecret, accountId, "", false, true, callback);
    }

    /**
     * Return the list of all transactions from transactionId for an Account of a Connection.
     * The list not includes pending transactions
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection
     * @param accountId - Account ID
     * @param fromTransactionId - the id from which the result list should starts
     * @param callback - callback for request result
     */
    public void fetchAllTransactions(String customerSecret,
                                     String connectionSecret,
                                     String accountId,
                                     String fromTransactionId,
                                     FetchTransactionsResult callback) {
        fetchTransactions(customerSecret, connectionSecret, accountId, fromTransactionId, false, true, callback);
    }

    /**
     * Return the single page of transactions from transactionId for an Account of a Connection.
     * Transaction page contains maximum 100 items.
     * The list not includes pending transactions
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection
     * @param accountId - Account ID
     * @param fromTransactionId - the ID from which the next page of transactions starts
     * @param callback - callback for request result
     */
    public void fetchTransactions(String customerSecret,
                                  String connectionSecret,
                                  String accountId,
                                  String fromTransactionId,
                                  FetchTransactionsResult callback) {
        fetchTransactions(customerSecret, connectionSecret, accountId, fromTransactionId, false, false, callback);
    }

    /**
     * Return the list of transactions for an Account of a Connection.
     * Initiate transactions fetch by params.
     * The list includes posted or pending transactions.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection
     * @param accountId - Account ID
     * @param fromTransactionId - the ID from which the next page of transactions starts
     * @param fetchPendingTransactions - flag which indicates what type of transactions should be fetched (Normal or Pending)
     * @param fetchAllTransactionsFromId - flag which indicates that should be fetched one page or all
     * @param callback - callback for request result
     */
    public void fetchTransactions(String customerSecret,
                                  String connectionSecret,
                                  String accountId,
                                  String fromTransactionId,
                                  boolean fetchPendingTransactions,
                                  boolean fetchAllTransactionsFromId,
                                  FetchTransactionsResult callback) {
        new TransactionsConnector(callback).fetchTransactions(
                customerSecret,
                connectionSecret,
                accountId,
                fromTransactionId,
                fetchPendingTransactions,
                fetchAllTransactionsFromId);
    }

    /**
     * Return the list of all pending transactions for an Account of a Connection.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret of the Connection
     * @param accountId - Account ID
     * @param callback - callback for request result
     */
    public void fetchPendingTransactionsOfAccount(String customerSecret,
                                                  String connectionSecret,
                                                  String accountId,
                                                  FetchTransactionsResult callback) {
        fetchPendingTransactionsOfAccount(customerSecret, connectionSecret, accountId, "", callback);
    }

    /**
     * Return the list of all pending transactions from transactionId for an Account of a Connection.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret of the Connection
     * @param accountId - Account ID
     * @param fromTransactionId - the id from which the result list should starts
     * @param callback - callback for request result
     */
    public void fetchPendingTransactionsOfAccount(String customerSecret,
                                                  String connectionSecret,
                                                  String accountId,
                                                  String fromTransactionId,
                                                  FetchTransactionsResult callback) {
        fetchTransactions(customerSecret, connectionSecret, accountId, fromTransactionId, true, true, callback);
    }

    /**
     * You can see the list of consents of Connection.
     * The accounts are sorted in ascending order of their ID, so the newest accounts will come last.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection
     * @param callback - callback for request result
     */
    public void fetchConsents(String customerSecret,
                              String connectionSecret,
                              FetchConsentsResult callback) {
        new ConsentsConnector(callback).fetchConsents(customerSecret, connectionSecret);
    }

    /**
     * Consent revoke is an option that allows you to revoke a consent.
     * Result is returned through callback.
     *
     * @param customerSecret - current Customer secret code
     * @param connectionSecret - secret code of the Connection which should be deleted if exist
     * @param consentId - id of Consent which should be revoked
     * @param callback - callback for request result
     */
    public void revokeConsent(String customerSecret,
                              String connectionSecret,
                              String consentId,
                              DeleteEntryResult callback) {
        new ConsentRevokeConnector(callback).revokeConsent(customerSecret, connectionSecret, consentId);
    }
}
