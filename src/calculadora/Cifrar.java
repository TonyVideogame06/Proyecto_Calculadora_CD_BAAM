package calculadora;

public class Cifrar {
    private String getCifrado(String texto, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println("Error " + e.getMessage());
        }
        return "";
    }


    public String md5(String texto) {
        return getCifrado(texto, "MD5");
    }

    public String sha1(String texto) {
        return getCifrado(texto, "SHA1");
    }
}
