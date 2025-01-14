package com.campus.utils.common;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {
    public static final Cipher decryptCipher;

    static {
        try {
            PrivateKey privateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate(
                            new PKCS8EncodedKeySpec(
                                    Base64.getDecoder()
                                            .decode(
                                                    "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKpgDCTRZOB3xtKz" +
                                                            "qmDm7BwQ/1URj3o9AtdCUDGpRifujoTU9fkbERDnmzWM9w5Jwq6gkrb/QEDm3dFM" +
                                                            "9QIqH0GGgaBgoBsZo324VVBQec7s6jcbzuaJy3CALxrEo1OUSKnre8us9Ey/WczY" +
                                                            "Y7MEGmbOSgI4dXKglWT8UqKSr4LfAgMBAAECgYEApWLrJuplJEjhJjPmFn7TgEM3" +
                                                            "uNp9Dsoe8dqbgjmxsp2ul18rDw+pN2G8lvUp9iK/60hQY3fQZbA2bzBnPhypjRm/" +
                                                            "97Dn7U4E3RGKcUgiEan+E+AdAlatPTXjQuvgsiXHpoXD5Lh+WH3RRspZUmXgLMSm" +
                                                            "WAkCLemFjEYF7bYHLJECQQDee/66FwKEbPneAMGSLMaPOB08wNaFOMHJizILeumt" +
                                                            "6YOsjkjHTzLrDFZAQwBdYAvbbhGjhms+PPnk2XAqT1uTAkEAxAp8lfdWUc9Z1ylN" +
                                                            "25cfrEDuGvYBUssl4E2VI8u/nAyCzPMmqjzygYV7sr38VZPJVxvrPZA4Id/ulx1O" +
                                                            "NLADBQJACcUsNIPaKnKWrnceCQtKVPPD5O0a/pK1f+JK+lIVfPuqOqOs8oO52rSZ" +
                                                            "/Qx0MaWMH8C/qYy3nO4Uk0YxNMpzXQJACz3GsxrBumM3dZ0Kt+LMkCYRAT7GCKdZ" +
                                                            "fqk0Oh8+14XoIjR3LR/dkAvFchRu2cFltDcHoDoa3eZnWgb2KxNEuQJADHbpj1Z5" +
                                                            "TfX6V4U+T59EEdkjUayA3rAy/icDC1QoeJ4wZNb++hZMeO2H8a1xqAb/5l6b3sxz" +
                                                            "8XDm3LbOlSuUBA==")
                            )
                    );
            decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String rsaDecrypt(String encrypted) throws IllegalBlockSizeException, BadPaddingException {
        return new String(decryptCipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String encrypted = "DltSUPr4vKblWekS9rKkTHQsRdOp4S2uufd8VALna6Y34eKuEWZ7LpSbymjG7W0ycinsbbCmz9ysqWyFbebQgCvMcXIo4EpAWVFmt7YP7tWzo1ruwV/leoVZFYw41QKY+xn3QaEy6Gz5kH2wELBx8Iuo/MJTd4EM/fZsaEossE8=";

        String res = rsaDecrypt(encrypted);

        System.out.println(res);
    }
}
