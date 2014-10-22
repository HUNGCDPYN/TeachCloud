/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.SwingUtilities;
import org.cloudbus.cloudsim.core.CloudSim;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;

@ActionID(category = "Build",
id = "jo.just.designer.RunAction")
@ActionRegistration(iconBase = "jo/just/designer/resources/run32.png",
displayName = "#CTL_RunAction")
@ActionReferences({
    @ActionReference(path = "Menu/Run", position = -90),
    @ActionReference(path = "Toolbars/Build", position = 0),
    @ActionReference(path = "Shortcuts", name = "D-R")
})
@Messages("CTL_RunAction=Run")
public final class RunAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
//        SLADialog sal = new SLADialog();
//        sal.main(null);

//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
////                redirectSystemStreams();
//                Interpreter i = new Interpreter(VisualDesignerTopComponent.getScene());
//            }
//
//        });
        
        Runnable run = new Runnable() {

            @Override
            public void run() {
                redirectSystemStreams();
                Interpreter i = new Interpreter(VisualDesignerTopComponent.getScene());
            }

        };
        new Thread(run).start();


//        CloudSimExample1.main(new String[]{""});
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {

            String name = "Results";

            @Override
            public void write(int i) throws IOException {
                IOProvider.getDefault().getIO(name, false).getOut().print(String.valueOf((char) i));
//                OutputHandler.output(outputName, String.valueOf((char) i));
            }

            @Override
            public void write(byte[] bytes) throws IOException {
                IOProvider.getDefault().getIO(name, false).getOut().print(new String(bytes));
//                OutputHandler.output(outputName, new String(bytes));
            }

            @Override
            public void write(byte[] bytes, int off, int len) throws IOException {
                IOProvider.getDefault().getIO(name, false).getOut().print(new String(bytes, off, len));
//                OutputHandler.output(outputName, new String(bytes, off, len));
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}
