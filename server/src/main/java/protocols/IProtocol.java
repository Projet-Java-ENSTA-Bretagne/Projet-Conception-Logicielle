package protocols;

import server.IContext;

import java.io.InputStream;
import java.io.OutputStream;

public interface IProtocol {
    void execute(IContext ctx, InputStream inStream, OutputStream outStream);
}
