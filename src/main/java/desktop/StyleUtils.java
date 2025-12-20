package desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class StyleUtils {
    // 1. Bảng màu thương hiệu (Hồng Pastel & Xám hiện đại)
    public static final Color PRIMARY_COLOR = new Color(219, 112, 147); // Pale Violet Red
    public static final Color SECONDARY_COLOR = new Color(255, 240, 245); // Lavender Blush
    public static final Color TEXT_COLOR = new Color(50, 50, 50);
    public static final Color WHITE = Color.WHITE;

    // 2. Phông chữ
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);

    // 3. Hàm tạo nút bấm đẹp
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(WHITE);
        btn.setBackground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover đơn giản
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_COLOR);
            }
        });
        return btn;
    }

    // 4. Hàm làm đẹp bảng (JTable)
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(FONT_REGULAR);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(TEXT_COLOR);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header đẹp
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 40));
        
        // Canh giữa dữ liệu
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    // 5. Định dạng tiền tệ VND
    public static String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) return "0 đ";
        Locale vn = new Locale("vi", "VN");
        return NumberFormat.getCurrencyInstance(vn).format(amount);
    }
 // Hàm 2: Nhận double (Thêm hàm này để fix lỗi của bạn)
    public static String formatCurrency(double amount) {
        // Chuyển đổi double sang BigDecimal rồi gọi hàm trên
        return formatCurrency(BigDecimal.valueOf(amount));
    }
    
    // Hàm 3: Nhận Double (Wrapper class)
    public static String formatCurrency(Double amount) {
        if (amount == null) return "0 đ";
        return formatCurrency(BigDecimal.valueOf(amount));
    }
}