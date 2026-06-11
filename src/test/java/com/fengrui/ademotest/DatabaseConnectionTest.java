package com.fengrui.ademotest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnection() throws Exception {
        System.out.println("=== 数据库连接测试 ===");
        
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ 数据库连接成功!");
            System.out.println("数据库URL: " + conn.getMetaData().getURL());
            System.out.println("数据库用户: " + conn.getMetaData().getUserName());
            System.out.println("数据库产品: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("数据库版本: " + conn.getMetaData().getDatabaseProductVersion());
            
            // 列出所有表
            System.out.println("\n=== 数据库表列表 ===");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, "public", "%", new String[]{"TABLE"});
            
            int count = 0;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("📋 表名: " + tableName);
                
                // 显示表的字段信息
                ResultSet columns = metaData.getColumns(null, "public", tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    String nullable = columns.getString("IS_NULLABLE");
                    System.out.println("   └─ " + columnName + " (" + dataType + ", " + nullable + ")");
                }
                columns.close();
                count++;
            }
            tables.close();
            
            System.out.println("\n✅ 共找到 " + count + " 张表");
        } catch (Exception e) {
            System.err.println(" 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
