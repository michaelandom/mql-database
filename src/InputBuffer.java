public class InputBuffer {
    private String buffer;
    private int inputLength;

    public int getInput_length() {
        return inputLength;
    }

    public String getBuffer() {
        return buffer;
    }

    public InputBuffer(String buffer) {
        this.buffer = buffer;
        this.inputLength = buffer.length() - 1;
    }


}


