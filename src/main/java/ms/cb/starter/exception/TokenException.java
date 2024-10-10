package ms.cb.starter.exception;


public class TokenException extends RuntimeException{
    public TokenException() {super();}

    public TokenException(String msg) {
        super(msg);
    }
}
