package protocols;

import server.IContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public interface IProtocol {
    void execute(IContext ctx, BufferedReader inStream, PrintStream outStream);
}
