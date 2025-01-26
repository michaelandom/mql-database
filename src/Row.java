
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
public class Row {
    private  int id;
    private String username;
    private String email;
    public static final int ID_SIZE = Integer.BYTES;
    public static final int USERNAME_SIZE = sizeOfAttribute("username");
    public static final int EMAIL_SIZE = sizeOfAttribute("email");
    public static final int ID_OFFSET = 0;
    public static final int USERNAME_OFFSET = ID_OFFSET + ID_SIZE;
    public static final int EMAIL_OFFSET = USERNAME_OFFSET + USERNAME_SIZE;
    public static final int ROW_SIZE = ID_SIZE + USERNAME_SIZE + EMAIL_SIZE;


    public Row(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(ROW_SIZE);
        buffer.putInt(id);
        buffer.put(username.getBytes(StandardCharsets.UTF_8));
        buffer.put(new byte[USERNAME_SIZE - username.length()]); // Padding
        buffer.put(email.getBytes(StandardCharsets.UTF_8));
        buffer.put(new byte[EMAIL_SIZE - email.length()]); // Padding
        return buffer.array();
    }

    public static Row deserialize(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int id = buffer.getInt();
        byte[] usernameBytes = new byte[USERNAME_SIZE];
        buffer.get(usernameBytes);
        String username = new String(usernameBytes, StandardCharsets.UTF_8).trim();
        byte[] emailBytes = new byte[EMAIL_SIZE];
        buffer.get(emailBytes);
        String email = new String(emailBytes, StandardCharsets.UTF_8).trim();
        return new Row(id, username, email);
    }

    public static void displayRowInfo() {
        System.out.println("ID Size: " + ID_SIZE);
        System.out.println("Username Size: " + USERNAME_SIZE);
        System.out.println("Email Size: " + EMAIL_SIZE);
        System.out.println("ID Offset: " + ID_OFFSET);
        System.out.println("Username Offset: " + USERNAME_OFFSET);
        System.out.println("Email Offset: " + EMAIL_OFFSET);
        System.out.println("Row Size: " + ROW_SIZE);
    }


    private static int sizeOfAttribute(String attribute) {
        return attribute != null ? attribute.length() : 0;
    }






}
