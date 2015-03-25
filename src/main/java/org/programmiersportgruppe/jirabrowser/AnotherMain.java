package org.programmiersportgruppe.jirabrowser;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import org.programmiersportgruppe.swingutils.UIUtils;

import javax.swing.*;

/**
 * Created by fleipold on 26/03/2015.
 */
public class AnotherMain {
    public static void main(String[] args) {

        DefaultFormBuilder formBuilder = new DefaultFormBuilder(new FormLayout("fill:100dlu:grow", "fill:pref:grow"));
        JTextArea textArea = new JTextArea("Here is the problem: for many years, the Supreme Court wrestled with\n" +
                "the issue of pornography, until finally Associate Justice John Paul\n" +
                "Stevens came up with the famous quotation about how he couldn't define\n" +
                "pornography, but he knew it when he saw it.  So for a while, the\n" +
                "court's policy was to have all the suspected pornography trucked to\n" +
                "Justice Stevens' house, where he would look it over.  \"Nope, this isn't\n" +
                "it,\" he'd say.  \"Bring some more.\"  This went on until one morning when\n" +
                "his housekeeper found him trapped in the recreation room under an\n" +
                "enormous mound of rubberized implements, and the court had to issue a\n" +
                "ruling stating that it didn't know what the hell pornography was except\n" +
                "that it was illegal and everybody should stop badgering the court about\n" +
                "it because the court was going to take a nap.\n" +
                "\t\t-- Dave Barry, \"Pornography\"\n" +
                "example-apps 0 $ fortune -o\n" +
                "I'm going to Iowa for an award.  Then I'm appearing at Carnegie Hall,\n" +
                "it's sold out.  Then I'm sailing to France to be honored by the French\n" +
                "government -- I'd give it all up for one erection.\n" +
                "\t\t-- Groucho Marx");
        formBuilder.append(textArea);
        System.out.println(textArea.getPreferredSize());
        System.out.println(textArea.getMinimumSize());
        textArea.addPropertyChangeListener("preferredSize", evt -> System.out.println(textArea.getPreferredSize()));
        textArea.addPropertyChangeListener("minimumSize", evt -> System.out.println(textArea.getMinimumSize()));

        formBuilder.border(Borders.DIALOG);
        textArea.setColumns(30);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        UIUtils.showInFrame(formBuilder.build());

    }
}
