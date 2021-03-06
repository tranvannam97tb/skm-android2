package jp.co.soliton.keymanager;

import android.content.Context;
import android.security.KeyChain;
import android.security.KeyChainException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLUtils {

	public static final String TAG = "SSLUtils";
	
	
	private static abstract class StubKeyManager extends X509ExtendedKeyManager {
        @Override
        public abstract String chooseClientAlias(
                String[] keyTypes, Principal[] issuers, Socket socket);

        @Override
        public abstract X509Certificate[] getCertificateChain(String alias);

        @Override
        public abstract PrivateKey getPrivateKey(String alias);


        // The following methods are unused.

        @Override
        public final String chooseServerAlias(
                String keyType, Principal[] issuers, Socket socket) {
            // not a client SSLSocket callback
            throw new UnsupportedOperationException();
        }

        @Override
        public final String[] getClientAliases(String keyType, Principal[] issuers) {
            // not a client SSLSocket callback
            throw new UnsupportedOperationException();
        }

        @Override
        public final String[] getServerAliases(String keyType, Principal[] issuers) {
            // not a client SSLSocket callback
            throw new UnsupportedOperationException();
        }
    }

	
	/**
     * A dummy {@link KeyManager} which keeps track of the last time a server has requested
     * a client certificate.
     */
    public static class TrackingKeyManager extends StubKeyManager {
        private volatile long mLastTimeCertRequested = 0L;

        @Override
        public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
            mLastTimeCertRequested = System.currentTimeMillis();
            return null;
        }

        @Override
        public X509Certificate[] getCertificateChain(String alias) {
            return null;
        }

        @Override
        public PrivateKey getPrivateKey(String alias) {
            return null;
        }

        /**
         * @return the last time that this {@link KeyManager} detected a request by a server
         *     for a client certificate (in millis since epoch).
         */
        public long getLastCertReqTime() {
            return mLastTimeCertRequested;
        }
    }

    /**
     * A {@link KeyManager} that reads uses credentials stored in the system {@link KeyChain}.
     */
    public static class KeyChainKeyManager extends StubKeyManager {
        private final String mClientAlias;
        private final X509Certificate[] mCertificateChain;
        private final PrivateKey mPrivateKey;

        /**
         * Builds an instance of a KeyChainKeyManager using the given certificate alias.
         * If for any reason retrieval of the credentials from the system {@link KeyChain} fails,
         * a {@code null} value will be returned.
         */
        public static KeyChainKeyManager fromAlias(Context context, String alias)
                throws CertificateException {
            X509Certificate[] certificateChain;
            try {
                certificateChain = KeyChain.getCertificateChain(context, alias);
            } catch (KeyChainException e) {
                throw new CertificateException(e);
            } catch (InterruptedException e) {
                throw new CertificateException(e);
            }

            PrivateKey privateKey;
            try {
                privateKey = KeyChain.getPrivateKey(context, alias);
            } catch (KeyChainException e) {
                throw new CertificateException(e);
            } catch (InterruptedException e) {
                throw new CertificateException(e);
            }

            if (certificateChain == null || privateKey == null) {
                throw new CertificateException("Can't access certificate from keystore");
            }

            return new KeyChainKeyManager(alias, certificateChain, privateKey);
        }

        private KeyChainKeyManager(
                String clientAlias, X509Certificate[] certificateChain, PrivateKey privateKey) {
            mClientAlias = clientAlias;
            mCertificateChain = certificateChain;
            mPrivateKey = privateKey;
        }


        @Override
        public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
            return mClientAlias;
        }

        @Override
        public X509Certificate[] getCertificateChain(String alias) {
            return mCertificateChain;
        }

        @Override
        public PrivateKey getPrivateKey(String alias) {
            return mPrivateKey;
        }
    }
}
