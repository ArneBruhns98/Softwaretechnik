package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorPopup extends JFrame{

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public ColorPopup(ControllerRoom conroom, ModelRoom room){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color");
        setBounds(100, 100, 750, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 1, 0, 0));

        for(int i = 0; i < room.getColors().size(); i++){
            JButton colorButton = new JButton();
            colorButton.setBackground(room.getColors().get(i));
            contentPane.add(colorButton);

                colorButton.setOpaque(true);                       //ToDO CRHIIIIIS!  Teste mal mit mac; teste sonst mal mit True
                colorButton.setBorderPainted(false);                //ToDO CRHRIIIIIIS?!  Teste mal ^-^
            
            int finalInt = i;
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    room.selectColor(room.getColors().get(finalInt));
                    dispose();
                }
            });
        }
    }
}


