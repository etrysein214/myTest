package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.*;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.*;

/**
 * AutoInputProgram 類別為自動輸入程式，繼承自 JFrame 並實作 NativeKeyListener 和 NativeMouseInputListener 介面。
 */
public class AutoInputProgram extends JFrame implements NativeKeyListener, NativeMouseInputListener {
    private Robot robot;
    private JTextField[] textFields;
    private JCheckBox enterCB;
    private JCheckBox tabCB;

    /**
     * AutoInputProgram 的建構子，初始化使用者介面。
     */
    public AutoInputProgram() {
        initializeUI();
    }

    /**
     * 初始化使用者介面的方法。
     */
    private void initializeUI() {
        setTitle("自動輸入程式");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400); // 調整視窗高度
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        addTitle(gbc);
        addTextFields(gbc);
        addCheckboxes(gbc);

//        // 調整字型大小
//        Font font = new Font("Microsoft JhengHei", Font.PLAIN, 40);
//
//        for (JTextField textField : textFields) {
//            textField.setFont(font);
//        }
//
//        // 調整外觀
//        UIManager.put("CheckBox.font", font); // 調整勾選方塊字型大小

        setVisible(true);
    }

    /**
     * 在介面上新增標題標籤。
     */
    private void addTitle(GridBagConstraints gbc) {
        JLabel title = new JLabel("按F2執行自動輸入");
        title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 25)); // 調整標題字型
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // 跨足三列
        gbc.insets = new Insets(10, 0, 20, 0); // 調整標題上方的空隙
        add(title, gbc);
    }

    /**
     * 在介面上新增文本欄位。
     */
    private void addTextFields(GridBagConstraints gbc) {
        String[] labels = {"文本1:", "文本2:", "文本3:", "文本4:", "文本5:"};
        textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            addLabel(labels[i], gbc, i);
            addTextField(gbc, i);
        }
    }

    /**
     * 在介面上新增標籤。
     */
    private void addLabel(String label, GridBagConstraints gbc, int index) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 25)); // 調整標題字型
        gbc.gridx = 0;
        gbc.gridy = index + 1;
        gbc.gridwidth = 1; // 恢復單列寬度
        gbc.insets = new Insets(10, 10, 0, 10);
        add(jLabel, gbc);
    }

    /**
     * 在介面上新增文本欄位。
     */
    private void addTextField(GridBagConstraints gbc, int index) {
        textFields[index] = new JTextField();
        textFields[index].setPreferredSize(new Dimension(200, 30));
        textFields[index].setFont(new Font("Microsoft JhengHei", Font.BOLD, 25)); // 調整標題字型
        gbc.gridx = 1;
        gbc.gridy = index + 1;
        add(textFields[index], gbc);
    }

    /**
     * 在介面上新增勾選方塊。
     */
    private void addCheckboxes(GridBagConstraints gbc) {
        enterCB = new JCheckBox("加入Enter鍵");
        enterCB.setFont(new Font("Microsoft JhengHei", Font.BOLD, 25)); // 調整標題字型
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 0, 10); // 調整勾選方塊右側的空隙
        add(enterCB, gbc);

        tabCB = new JCheckBox("加入Tab鍵");
        tabCB.setFont(new Font("Microsoft JhengHei", Font.BOLD, 25)); // 調整標題字型
        gbc.gridx = 2;
        gbc.gridy = 2;
        add(tabCB, gbc);
    }

    /**
     * 設定全域鉤子。
     */
    public void setupGlobalScreen() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("註冊本地鉤子時出現問題。");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
        GlobalScreen.addNativeKeyListener(this);
    }

    /**
     * 輸入文字的方法。
     */
    private void typeText(final String text) throws AWTException {
        this.robot = new Robot();
        final HashMap<Character, int[]> specialKeys = new HashMap<>();
        specialKeys.put('!', new int[] { 16, 49 });
        specialKeys.put('@', new int[] { 16, 50 });
        specialKeys.put('#', new int[] { 16, 51 });
        specialKeys.put('$', new int[] { 16, 52 });
        specialKeys.put('%', new int[] { 16, 53 });
        specialKeys.put('^', new int[] { 16, 54 });
        specialKeys.put('&', new int[] { 16, 55 });
        specialKeys.put('*', new int[] { 16, 56 });
        specialKeys.put('(', new int[] { 16, 57 });
        specialKeys.put(')', new int[] { 16, 48 });
        specialKeys.put('_', new int[] { 16, 45 });
        specialKeys.put('+', new int[] { 521 });
        specialKeys.put('=', new int[] { 61 });
        this.robot.keyRelease(18);
        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            if (Character.isUpperCase(c)) {
                this.robot.keyPress(16);
                this.robot.delay(300);
                this.robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
                this.robot.delay(300);
                this.robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
                this.robot.delay(300);
                this.robot.keyRelease(16);
                this.robot.delay(300);
            }
            else if (specialKeys.containsKey(c)) {
                final int[] keyCodes = specialKeys.get(c);
                this.robot.keyPress(specialKeys.get(c)[0]);
                this.robot.delay(300);
                this.robot.keyPress(specialKeys.get(c)[1]);
                this.robot.delay(300);
                this.robot.keyRelease(specialKeys.get(c)[1]);
                this.robot.delay(300);
                this.robot.keyRelease(specialKeys.get(c)[0]);
                this.robot.delay(300);
            }
            else {
                this.robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
                this.robot.delay(300);
                this.robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
                this.robot.delay(300);
            }
            this.robot.delay(300);
        }
    }

    /**
     * 主程式進入點。
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("註冊本地鉤子時出現問題。");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        final AutoInputProgram mc = new AutoInputProgram();
        GlobalScreen.addNativeMouseListener((NativeMouseListener) mc);
        GlobalScreen.addNativeMouseMotionListener((NativeMouseMotionListener) mc);
        GlobalScreen.addNativeKeyListener((NativeKeyListener) mc);
    }

    /**
     * NativeKeyListener 介面的方法，處理按鍵被按下事件。
     */
    public void nativeKeyPressed(final NativeKeyEvent e) {
        try {
            if ((e.getModifiers() & 0x88) != 0x0) {
                if (e.getKeyCode() == 2) {
                    this.typeText(textFields[0].getText());
                }
                else if (e.getKeyCode() == 3) {
                    this.typeText(textFields[1].getText());
                }
                else if (e.getKeyCode() == 4) {
                    this.typeText(textFields[2].getText());
                }
                else if (e.getKeyCode() == 5) {
                    this.typeText(textFields[3].getText());
                }
            }
            if (e.getKeyCode() == 62) {
                String str = textFields[4].getText();
                int number = Integer.parseInt(str);
                str = Integer.toString(++number);
                copyToClipboard(str);
                textFields[4].setText(str);
                (this.robot = new Robot()).keyPress(17);
                this.robot.keyPress(86);
                this.robot.keyRelease(86);
                this.robot.keyRelease(17);
            }
            if (e.getKeyCode() == 60) {
                this.typeText(textFields[0].getText());
                this.robot.delay(300);
                if (this.tabCB.isSelected()) {
                    this.robot.keyPress(9);
                    this.robot.delay(300);
                }
                this.typeText(textFields[1].getText());
                this.robot.delay(300);
                if (this.enterCB.isSelected()) {
                    this.robot.keyPress(10);
                    this.robot.delay(300);
                }
            }
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * NativeKeyListener 介面的方法，處理按鍵被釋放事件。
     */
    public void nativeKeyReleased(final NativeKeyEvent e) {
    }

    /**
     * NativeKeyListener 介面的方法，處理按鍵被打字事件。
     */
    public void nativeKeyTyped(final NativeKeyEvent e) {
    }

    // NativeMouseInputListener 介面的其他方法...

    /**
     * 停止偵聽滑鼠事件的方法。
     */
    public void cancelMouseListening() {
        GlobalScreen.removeNativeMouseListener((NativeMouseListener) this);
        GlobalScreen.removeNativeMouseMotionListener((NativeMouseMotionListener) this);
    }

    /**
     * 複製文字到剪貼簿的方法。
     */
    private static void copyToClipboard(final String text) {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, selection);
    }

    /**
     * 從剪貼簿取得文字的方法。
     */
    private static String getFromClipboard() {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable transferable = clipboard.getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex2) {
                ex2.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 處理滑鼠點擊事件。
     *
     * @param nativeEvent 本地滑鼠點擊事件
     */
    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
        NativeMouseInputListener.super.nativeMouseClicked(nativeEvent);
    }

    /**
     * 處理滑鼠按下事件。
     *
     * @param nativeEvent 本地滑鼠按下事件
     */
    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        // 獲取按下的滑鼠按鍵和座標
        int button = nativeEvent.getButton();
        int x = nativeEvent.getX();
        int y = nativeEvent.getY();

        // 在這裡加入處理滑鼠按下事件的程式碼
        System.out.println("滑鼠按下 - 按鍵: " + button + ", 座標: (" + x + ", " + y + ")");
    }

    /**
     * 處理滑鼠釋放事件。
     *
     * @param nativeEvent 本地滑鼠釋放事件
     */
    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        // 獲取釋放的滑鼠按鍵和座標
        int button = nativeEvent.getButton();
        int x = nativeEvent.getX();
        int y = nativeEvent.getY();

        // 在這裡加入處理滑鼠釋放事件的程式碼
        System.out.println("滑鼠釋放 - 按鍵: " + button + ", 座標: (" + x + ", " + y + ")");
    }


    /**
     * 處理滑鼠移動事件。
     *
     * @param nativeEvent 本地滑鼠移動事件
     */
    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
        // 獲取滑鼠移動的座標
        int x = nativeEvent.getX();
        int y = nativeEvent.getY();

        // 在這裡加入處理滑鼠移動事件的程式碼
        System.out.println("滑鼠移動 - 座標: (" + x + ", " + y + ")");
    }


    /**
     * 處理滑鼠拖曳事件。
     *
     * @param nativeEvent 本地滑鼠拖曳事件
     */
    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
        // 獲取滑鼠拖曳的座標
        int x = nativeEvent.getX();
        int y = nativeEvent.getY();

        // 在這裡加入處理滑鼠拖曳事件的程式碼
        System.out.println("滑鼠拖曳 - 座標: (" + x + ", " + y + ")");
    }

}
