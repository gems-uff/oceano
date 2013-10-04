/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.controller.servlet;

import br.uff.ic.oceano.core.service.ProtocolService;
import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.controller.servlet.command.CommandFactory;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Heliomar
 */
public class JSONServlet extends HttpServlet {

    private static final String CMD = "cmd";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cmd = request.getParameter(CMD);
        System.out.println("entrou no get, command = " + cmd);

        if (cmd == null || cmd.trim().isEmpty() && (!cmd.equals(Command.ADMIN_CARGA_DEFAULT) || !cmd.equals(Command.PREPARE_BD_TO_EXPERIMENTS_PEIXE_ESPADA))) {
            response.getWriter().println("<h1 align='center'>This Servlet is avaiable only to PeixeEspadaCliente and Ouriço</h1>");
            return;
        }

        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cmd = req.getParameter(CMD);
        System.out.println("entrou no POST, command = " + cmd);

        if (cmd == null || cmd.trim().isEmpty()) {
            return;
        }

        Command command = CommandFactory.getCommand(cmd);
        if (command == null) {
            writeError(resp, cmd, new RuntimeException("Command not found"));
        } else {
            try {
                command.execute(req, resp);
            } catch (Exception ex) {
                Logger.getLogger(JSONServlet.class.getName()).log(Level.SEVERE, null, ex);
                writeError(resp, cmd, ex);
            }
        }

    }

    private void writeError(HttpServletResponse resp, String cmd, Throwable th) throws IOException {
        PrintWriter out = resp.getWriter();
        out.print(ProtocolService.CMD_EXCEPTION);
        out.print("Error for command [" + cmd + "] in Server:\n");
        out.print(th.getMessage());
        StringBuilder stringBuilder = new StringBuilder("");

        printError(th, stringBuilder);
        out.println(stringBuilder.toString());
    }

    public void printError(Throwable th, StringBuilder error) {
        error.append("    ").append(th.getMessage());
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            error.append("      ").append(stackTraceElement.toString());
        }
        if (th.getCause() != null) {
            printError(th.getCause(), error);
        }


    }
}
