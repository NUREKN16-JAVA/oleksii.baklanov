package baklanov.usermanagement.gui;

import baklanov.usermanagement.User;
import baklanov.usermanagement.db.DaoFactory;
import baklanov.usermanagement.db.UserDao;
import baklanov.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private JPanel contentPanel;
    private BrowsePanel browsePanel;
    private AddPanel addPanel;
    private EditPanel editPanel;
    private DeletePanel deletePanel;
    private DetailsPanel detailsPanel;
    private UserDao dao;

    public MainFrame(){
        super();
        dao= DaoFactory.getInstance().getUserDao();
        initialize();
    }

    public UserDao getDao() {
        return dao;
    }

    private void initialize() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        this.setTitle(Messages.getString("user.management"));
        this.setContentPane(getContentPanel());
    }

    private JPanel getContentPanel() {
        if(contentPanel==null){
            contentPanel=new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getBrowsePanel(),BorderLayout.CENTER);
        }
        return contentPanel;
    }

    private JPanel getBrowsePanel() {
        if(browsePanel==null){
            browsePanel=new BrowsePanel(this);
        }
        browsePanel.initTable();
        return browsePanel;
    }

    public static void main(String []args) {
        MainFrame frame=new MainFrame();
        frame.setVisible(true);
    }

    public void showAddPanel() {
        showPanel(getAddPanel());
    }

    public void showEditPanel(User user) {
        EditPanel editPanel= getEditPanel();
        editPanel.showEditPanel(user);
        showPanel(getEditPanel());
    }

    public void showDeletePanel(User user) {
        DeletePanel deletePanel= getDeletePanel();
        deletePanel.showDeletePanel(user);
        showPanel(getDeletePanel());
    }



    private void showPanel(JPanel panel) {
        getContentPanel().add(panel,BorderLayout.CENTER);
        panel.setVisible(true);
        panel.repaint();
    }

    private AddPanel getAddPanel() {
        if(addPanel==null){
            addPanel=new AddPanel(this);
        }
        return addPanel;
    }

    private EditPanel getEditPanel() {
        if(editPanel==null){
            editPanel=new EditPanel(this);
        }
        return editPanel;
    }

    private DeletePanel getDeletePanel() {
        if(deletePanel==null){
            deletePanel=new DeletePanel(this);
        }
        return deletePanel;
    }


    public void showBrowsePanel() {
        showPanel(getBrowsePanel());
    }

    public void showDetailsPanel(User user) {
        DetailsPanel detailsPanel = getDetailsPanel();
        detailsPanel.showDetailsPanel(user);
        this.showPanel(getDetailsPanel());
    }



    public DetailsPanel getDetailsPanel() {
        if (detailsPanel == null) {
            detailsPanel = new DetailsPanel(this);

        }
        return detailsPanel;
    }

}
