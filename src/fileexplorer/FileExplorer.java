/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer;






import com.sun.org.apache.xml.internal.dtm.DTM;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.SwingConstants.TOP;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Arafat
 */

interface choose
{
    public JTable getTable();
    public JList getList();
}


class createTable implements choose
{
    private JTable table;
    
    
    private createTable()
    {
        table=new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(true);
        
    }
    private static createTable instance = new createTable();
    public static createTable getInst()
    {
        return instance;
    }
    public JTable getTable()
    {
        return table;
    }

    @Override
    public JList getList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


class Lists implements choose
{
    private JList jl;
    
    private Lists()
    {  
        jl=new JList();
        jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jl.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jl.setVisibleRowCount(-1);
 
    }
    private static Lists instance = new Lists();
    public static Lists getListInstance()
    {
        return instance;
    }
    
    public JList getList()
    {
        return jl;
    }

    @Override
    public JTable getTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class viewSelect
{
    private JList l;
    private JTable t;
    viewSelect(){
        Lists ls=Lists.getListInstance();
        l=ls.getList();
        createTable ct=createTable.getInst();
        t=ct.getTable();
    }
    public JList sendList()
    {
        return l;
    }
    public JTable sendTable()
    {
        return t;
    }
}


class treeView
{
    private JProgressBar progress;
    private JTree tree;
    private JTable jt;
    private DefaultTreeModel dtm;
    private FileSystemView fsView;
    private Table_of_File tof;
    JMenu jmView=new JMenu("View");
    JMenuBar jmb=new JMenuBar();
    JMenuItem jmv1=new JMenuItem("Tiles");
    JMenuItem jmv2=new JMenuItem("Details");
    private ListSelectionListener lsl;
    private File cFile;
    private JLabel name;
    private JTextField path;
    private JPanel panel;
    private JList list;
    private JScrollPane jp;
    private JScrollPane spane;
    private boolean x=false;
    
    treeView()
    {
        createTable t=createTable.getInst();
        Lists l=Lists.getListInstance();
        jt=t.getTable();
        list=l.getList();
        tree=new JTree();
        progress=new JProgressBar();
        
    }
    public JPanel treeDesign()
    {
        panel = new JPanel(new BorderLayout(0,0));
        panel.setBackground(Color.lightGray);
        panel.setBorder(new EmptyBorder(8,10,10,8));
        fsView = FileSystemView.getFileSystemView();
        JPanel view = new JPanel(new BorderLayout(3,3));
        viewSelect ve=new viewSelect();
        ve.sendList().setCellRenderer(new MyCellRenderer());
        jmView.add(jmv1);
        jmView.add(jmv2);
        jmb.add(jmView);
        jmv1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                spane.setViewportView(ve.sendList());
                System.out.println("here");
            }
        });
        jmv2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                 spane.setViewportView(ve.sendTable());
                 System.out.println("there");
            }
        });
        spane = new JScrollPane(ve.sendTable());
        spane.setBackground(Color.lightGray);
        spane.setPreferredSize(new Dimension(500,200));
        spane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        view.add(spane, BorderLayout.SOUTH);
        
        DefaultMutableTreeNode x = new DefaultMutableTreeNode();
        dtm=new DefaultTreeModel(x);
        TreeSelectionListener tsListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode tree_node =(DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                nextFolders(tree_node);
                fileDetails((File)tree_node.getUserObject());
            }
        };

        File[] fi = fsView.getRoots();
        for (File y : fi) {
            DefaultMutableTreeNode abc = new DefaultMutableTreeNode(y);
            x.add(abc);
            File[] xyz = fsView.getFiles(y, true);
            for (File p : xyz) {
                if (p.isDirectory()) {
                    abc.add(new DefaultMutableTreeNode(p));
                }
            }
        }

        tree = new JTree(dtm);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(tsListener);
        tree.expandRow(0);
        tree.setVisible(true);
        jp = new JScrollPane(tree);
        
        
        
        Dimension s=new Dimension(200,200);
        jp.setPreferredSize(s);
        JPanel panelSize = new JPanel(new BorderLayout(0,0));
        panelSize.setBorder(new EmptyBorder(0,3,0,100));

        JPanel fh = new JPanel(new GridLayout(0,1,2,2));
        panelSize.add(fh, BorderLayout.EAST);
        fh.add(jmb);

        JPanel label = new JPanel(new GridLayout(0,1,1,1));
        panelSize.add(label, BorderLayout.WEST);

        JPanel fd = new JPanel(new GridLayout(0,1,2,2));
        panelSize.add(fd, BorderLayout.CENTER);
        name = new JLabel();
        label.add(new JLabel("Path: ", JLabel.TRAILING));
        //label.add(jmb);
        path = new JTextField(5);
        path.setEditable(false);
        fd.add(path);
        JPanel fvw = new JPanel(new BorderLayout(3,3));
        fvw.add(panelSize,BorderLayout.CENTER);
        view.add(fvw, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jp,view);
        panel.add(splitPane, BorderLayout.CENTER);



        progress = new JProgressBar();
        progress.setVisible(false);

        
        
    
        return panel;

    } 
    private void store_data(File [] file)
    {

        if(tof==null)
        {
            tof=new Table_of_File();
            jt.setModel(tof);
            File []f=new File(".").listFiles();
            for (int i = 0; i < f.length; i++) {
                        if (f[i].isFile()||f[i].isDirectory()) {
                            list.setListData(f);
                        }
                    };
            
            
            tof.file_value(f);
        }
        else{
           jt.setModel(tof);
           tof.file_value(file);
        }
    }
    
    private void fileDetails(File file) {
        cFile = file;
        name.setText(fsView.getSystemDisplayName(file));
        if(x==false)
        {
            String s=System.getProperty("user.dir");
            path.setText(s);
            x=true;
        }
        else{
        path.setText(file.getPath());}
        panel.repaint();
    }
    
    private static class MyCellRenderer extends DefaultListCellRenderer  {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof File) {
                File file = (File) value;
                String s=file.getName();
                if(s.length()>12)
                {
                    s=s.substring(0, 9)+"...";
                }
                setText(s);
                setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled());
                setFont(list.getFont().deriveFont(TOP));
                setOpaque(true);
            }
            return this;
        }
    }
    
    public void ensure() {
        tree.setSelectionInterval(0,0);
    }
    
    private void nextFolders(DefaultMutableTreeNode c) {
        tree.setEnabled(false);
        progress.setVisible(true);
        progress.setIndeterminate(true);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            protected Void doInBackground() {
                File file = (File) c.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fsView.getFiles(file, true);
                    if (c.isLeaf()) {
                        for (File next : files) {
                            if (next.isDirectory()) {
                                publish(next);
                            }
                        }
                    }
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()||files[i].isDirectory()) {
                            list.setListData(files);
                        }
                    };
                    store_data(files);
                }
                return null;
            }

            @Override
            protected void process(List<File> x) {
                for (File p : x) {
                    c.add(new DefaultMutableTreeNode(p));
                }
            }

            @Override
            protected void done() {
                progress.setIndeterminate(false);
                progress.setVisible(false);
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }
}

class Table_of_File extends AbstractTableModel
    {
        public File[] file;
        public FileSystemView fsView=FileSystemView.getFileSystemView();
        String[] names={"Icon","File","Size","Date Modified"};
        Table_of_File()
        {
            this(new File[0]);
        }
        Table_of_File(File [] file1)
        {
            file=file1;
        }
        public void file_value(File [] f)
        {
            file=f;
            fireTableDataChanged();
        }            
        @Override
        public int getRowCount() {
            return file.length;
        }

        @Override
        public int getColumnCount() {
            return names.length;
        }

        @Override
        public Object getValueAt(int r, int c) {
            File file2=file[r];
            if(c==0)
            {
                return fsView.getSystemIcon(file2);
            }
            else if(c==1)
            {
                return fsView.getSystemDisplayName(file2);
            }
            else if(c==2)
            {
                return file2.length();
            }
            else if(c==3)
            {
                return file2.lastModified();
            }
            return null;
        
        }
        public String getColumnName(int r)
        {
            return names[r];
        }
        public Class<?> getColumnClass(int c) {            
            if(c==0)
            {
                return ImageIcon.class;
            }
            else if(c==3)
            {
                return Date.class;
            }
            return String.class;
        }
    }


public class FileExplorer {

    /**
     * @param args the command line arguments
     */
    private static JFrame mainFra;

    
    static class mainFrame
    {
        private static final mainFrame instance = new mainFrame();
        
        
        private mainFrame()
        {
            
            treeView tv=new treeView();
            mainFra=new JFrame("ISD Offline: 1405106");
            mainFra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.out.println("sfde");
            mainFra.setContentPane(tv.treeDesign());
            mainFra.pack();
            mainFra.setLocationByPlatform(true);
            mainFra.setMinimumSize(mainFra.getSize());
            mainFra.setVisible(true);    
            tv.ensure();
        }       
        public static mainFrame getInstance()
        {
            return instance;
        }       

        
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        mainFrame mf=mainFrame.getInstance();   
    }
    
}



