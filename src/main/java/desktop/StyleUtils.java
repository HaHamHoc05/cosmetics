package desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class StyleUtils {
    // Màu sắc chủ đạo
    public static final Color PRIMARY_COLOR = new Color(216, 27, 96); // Hồng đậm
    public static final Color SECONDARY_COLOR = new Color(252, 228, 236); // Hồng phấn
    public static final Color TEXT_COLOR = new Color(0,0,0);

    // Font chữ
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);

    // Tạo Button theo phong cách chung
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Format tiền tệ VNĐ
    public static String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(amount);
    }

    // Style cho Table đẹp hơn
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(SECONDARY_COLOR);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.setFont(FONT_REGULAR);
        table.setSelectionBackground(new Color(232, 242, 254));
        table.setSelectionForeground(Color.BLACK);
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

	public static Object formatCurrency(BigDecimal price) {
		// TODO Auto-generated method stub
		return null;
	}
}