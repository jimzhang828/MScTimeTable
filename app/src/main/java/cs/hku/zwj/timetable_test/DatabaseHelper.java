package cs.hku.zwj.timetable_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建sql语句，新建表
        db.execSQL("DROP TABLE IF EXISTS sem2;");
        String sqlCommand1 = "CREATE TABLE sem2 (\n" +
                             "schedule_id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                             "courseId     TEXT,\n" +
                             "courseName   TEXT,\n" +
                             "scheduleDate DATE,\n" +
                             "classroom    TEXT,\n" +
                             "startTime    TIME,\n" +
                             "endTime      TEXT,\n" +
                             "bgColor      TEXT,\n" +
                             "weekNum      INT\n" +
                             ");";
        //执行sql语句
        db.execSQL(sqlCommand1);

        String[] insertions = {
                "insert into sem2 values (1, 'COMP7801', 'Topic in computer science', '2020-02-27', 'CB-A', '14:30', '17:30', '#76933C', 6);",
                "insert into sem2 values (2, 'COMP7606A', 'Deep Learning', '2020-03-4', 'CB-A', '9:30', '12:30', '#F4DF42', 7);",
                "insert into sem2 values (3, 'COMP7506B', 'Smart phone apps development', '2020-03-5', 'CB-A', '15:30', '18:30', '#FF8409', 7);",
                "insert into sem2 values (4, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-03-4', 'CB-A', '14:30', '17:30', '#CCECFF', 7);",
                "insert into sem2 values (5, 'COMP7404D', 'Computational intelligence and machine learning', '2020-03-5', 'CB-C', '19:00', '22:00', '#FF9999', 7);",
                "insert into sem2 values (6, 'COMP7408', 'Distributed ledger and blockchain technology', '2020-03-6', 'CB-C', '19:00', '22:00', '#66FF00', 7);",
                "insert into sem2 values (7, 'COMP7405', 'Techniques in computational finance', '2020-03-4', 'CB-A', '19:00', '22:00', '#00B050', 7);",
                "insert into sem2 values (8, 'COMP7606A', 'Deep Learning', '2020-03-11', 'CB-A', '9:30', '12:30', '#F4DF42', 8);",
                "insert into sem2 values (9, 'COMP7506B', 'Smart phone apps development', '2020-03-12', 'Online', '15:30', '18:30', '#FF8409', 8);",
                "insert into sem2 values (10, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-03-11', 'CB-A', '14:30', '17:30', '#CCECFF', 8);",
                "insert into sem2 values (11, 'COMP7801', 'Topic in computer science', '2020-03-14', 'CB-A', '14:30', '17:30', '#76933C', 8);",
                "insert into sem2 values (12, 'COMP7506A', 'Smart phone apps development', '2020-03-10', 'Online', '19:00', '22:00', '#FF99FF', 8);",
                "insert into sem2 values (13, 'COMP7405', 'Techniques in computational finance', '2020-03-11', 'CB-A', '19:00', '22:00', '#00B050', 8);",
                "insert into sem2 values (14, 'COMP7103B', 'Data mining', '2020-03-15', 'Recorded Lecture', '19:00', '22:00', '#00B0F0', 8);",
                "insert into sem2 values (15, 'COMP7606A', 'Deep Learning', '2020-03-18', 'CB-A', '9:30', '12:30', '#F4DF42', 9);",
                "insert into sem2 values (16, 'COMP7506B', 'Smart phone apps development', '2020-03-19', 'Online', '15:30', '18:30', '#FF8409', 9);",
                "insert into sem2 values (17, 'COMP7408', 'Distributed ledger and blockchain technology', '2020-03-21', 'Online', '9:30', '12:30', '#66FF00', 9);",
                "insert into sem2 values (18, 'COMP7404D', 'Computational intelligence and machine learning', '2020-03-18', 'CB-C', '19:00', '22:00', '#FF9999', 9);",
                "insert into sem2 values (19, 'COMP7408', 'Distributed ledger and blockchain technology', '2020-03-19', 'Online', '19:00', '22:00', '#66FF00', 9);",
                "insert into sem2 values (20, 'COMP7904', 'Information security: attacks and defense', '2020-03-21', 'Online', '14:00', '17:00', '#B66DF3', 9);",
                "insert into sem2 values (21, 'COMP7506A', 'Smart phone apps development', '2020-03-16', 'Online', '19:00', '22:00', '#FF99FF', 9);",
                "insert into sem2 values (22, 'COMP7103B', 'Data mining', '2020-03-17', 'Online', '19:00', '22:00', '#00B0F0', 9);",
                "insert into sem2 values (23, 'COMP7103B', 'Data mining', '2020-03-20', 'Online', '19:00', '22:00', '#00B0F0', 9);",
                "insert into sem2 values (24, 'COMP7103B', 'Data mining', '2020-03-21', 'Recorded Lecture', '19:00', '22:00', '#00B0F0', 9);",
                "insert into sem2 values (25, 'COMP7506B', 'Smart phone apps development', '2020-03-26', 'Online', '15:30', '18:30', '#FF8409', 10);",
                "insert into sem2 values (26, 'COMP7408', 'Distributed ledger and blockchain technology', '2020-03-28', 'Online', '9:30', '12:30', '#66FF00', 10);",
                "insert into sem2 values (27, 'COMP7407', 'Securities transaction banking', '2020-03-29', 'Recorded Lecture', '9:30', '12:30', '#FFFF00', 10);",
                "insert into sem2 values (28, 'COMP7606C', 'Deep Learning', '2020-03-23', 'Online', '19:00', '22:00', '#FABF8F', 10);",
                "insert into sem2 values (29, 'COMP7305B', 'Cluster and cloud computing', '2020-03-24', 'Online', '19:00', '22:00', '#FF3399', 10);",
                "insert into sem2 values (30, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-03-25', 'Online', '14:30', '17:30', '#CCECFF', 10);",
                "insert into sem2 values (31, 'COMP7404D', 'Computational intelligence and machine learning', '2020-03-26', 'Online', '19:00', '22:00', '#FF9999', 10);",
                "insert into sem2 values (32, 'COMP7408', 'Distributed ledger and blockchain technology', '2020-03-27', 'Online', '19:00', '22:00', '#66FF00', 10);",
                "insert into sem2 values (33, 'COMP7801', 'Topic in computer science', '2020-03-28', 'Online', '14:30', '17:30', '#76933C', 10);",
                "insert into sem2 values (34, 'COMP7407', 'Securities transaction banking', '2020-03-29', 'Online', '14:00', '17:00', '#FFFF00', 10);",
                "insert into sem2 values (35, 'COMP7901', 'Legal protection of digital property', '2020-03-23', 'Online', '19:00', '22:00', '#0DF9F9', 10);",
                "insert into sem2 values (36, 'COMP7506A', 'Smart phone apps development', '2020-03-24', 'Online', '19:00', '22:00', '#FF99FF', 10);",
                "insert into sem2 values (37, 'COMP7103B', 'Data mining', '2020-03-25', 'Online', '19:00', '22:00', '#00B0F0', 10);",
                "insert into sem2 values (38, 'COMP7904', 'Information security: attacks and defense', '2020-03-26', 'Online', '19:00', '22:00', '#B66DF3', 10);",
                "insert into sem2 values (39, 'COMP7606B', 'Deep Learning', '2020-03-27', 'Online', '19:00', '22:00', '#66FFCC', 10);",
                "insert into sem2 values (40, 'COMP7103B', 'Data mining', '2020-03-29', 'Recorded Lecture', '19:00', '22:00', '#00B0F0', 10);",
                "insert into sem2 values (41, 'COMP7606A', 'Deep Learning', '2020-03-31', 'Online', '15:00', '18:00', '#F4DF42', 11);",
                "insert into sem2 values (42, 'COMP7506B', 'Smart phone apps development', '2020-04-2', 'Online', '15:30', '18:30', '#FF8409', 11);",
                "insert into sem2 values (43, 'COMP7407', 'Securities transaction banking', '2020-04-5', 'Recorded Lecture', '9:30', '12:30', '#FFFF00', 11);",
                "insert into sem2 values (44, 'COMP7606C', 'Deep Learning', '2020-03-30', 'Online', '19:00', '22:00', '#FABF8F', 11);",
                "insert into sem2 values (45, 'COMP7305B', 'Cluster and cloud computing', '2020-03-31', 'Online', '19:00', '22:00', '#FF3399', 11);",
                "insert into sem2 values (46, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-04-1', 'Online', '14:30', '17:30', '#CCECFF', 11);",
                "insert into sem2 values (47, 'COMP7404D', 'Computational intelligence and machine learning', '2020-04-2', 'Online', '19:00', '22:00', '#FF9999', 11);",
                "insert into sem2 values (48, 'COMP7407', 'Securities transaction banking', '2020-04-4', 'Recorded Lecture', '14:00', '17:00', '#FFFF00', 11);",
                "insert into sem2 values (49, 'COMP7901', 'Legal protection of digital property', '2020-03-30', 'Online', '19:00', '22:00', '#0DF9F9', 11);",
                "insert into sem2 values (50, 'COMP7407', 'Securities transaction banking', '2020-03-31', 'Online', '19:00', '22:00', '#FFFF00', 11);",
                "insert into sem2 values (51, 'COMP7405', 'Techniques in computational finance', '2020-04-1', 'Online', '19:00', '22:00', '#00B050', 11);",
                "insert into sem2 values (52, 'COMP7103B', 'Data mining', '2020-04-2', 'Online', '19:00', '22:00', '#00B0F0', 11);",
                "insert into sem2 values (53, 'COMP7407', 'Securities transaction banking', '2020-04-3', 'Online', '19:00', '22:00', '#FFFF00', 11);",
                "insert into sem2 values (54, 'COMP7606B', 'Deep Learning', '2020-04-4', 'Online', '19:00', '22:00', '#66FFCC', 11);",
                "insert into sem2 values (55, 'COMP7606A', 'Deep Learning', '2020-04-7', 'Online', '15:00', '18:00', '#F4DF42', 12);",
                "insert into sem2 values (56, 'COMP7506B', 'Smart phone apps development', '2020-04-9', 'Online', '15:30', '18:30', '#FF8409', 12);",
                "insert into sem2 values (57, 'COMP7606C', 'Deep Learning', '2020-04-6', 'Online', '19:00', '22:00', '#FABF8F', 12);",
                "insert into sem2 values (58, 'COMP7305B', 'Cluster and cloud computing', '2020-04-7', 'Online', '19:00', '22:00', '#FF3399', 12);",
                "insert into sem2 values (59, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-04-8', 'Online', '14:30', '17:30', '#CCECFF', 12);",
                "insert into sem2 values (60, 'COMP7404D', 'Computational intelligence and machine learning', '2020-04-9', 'Online', '19:00', '22:00', '#FF9999', 12);",
                "insert into sem2 values (61, 'COMP7901', 'Legal protection of digital property', '2020-04-6', 'Online', '19:00', '22:00', '#0DF9F9', 12);",
                "insert into sem2 values (62, 'COMP7407', 'Securities transaction banking', '2020-04-7', 'Online', '19:00', '22:00', '#FFFF00', 12);",
                "insert into sem2 values (63, 'COMP7405', 'Techniques in computational finance', '2020-04-8', 'Online', '19:00', '22:00', '#00B050', 12);",
                "insert into sem2 values (64, 'COMP7103B', 'Data mining', '2020-04-9', 'Online', '19:00', '22:00', '#00B0F0', 12);",
                "insert into sem2 values (65, 'COMP7408', 'Distributed ledger and blockchain technology', '2020-04-18', 'Online', '10:00', '13:00', '#66FF00', 13);",
                "insert into sem2 values (66, 'COMP7407', 'Securities transaction banking', '2020-04-19', 'Recorded Lecture', '9:30', '12:30', '#FFFF00', 13);",
                "insert into sem2 values (67, 'COMP7305B', 'Cluster and cloud computing', '2020-04-14', 'Online', '19:00', '22:00', '#FF3399', 13);",
                "insert into sem2 values (68, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-04-15', 'Online', '14:30', '17:30', '#CCECFF', 13);",
                "insert into sem2 values (69, 'COMP7404D', 'Computational intelligence and machine learning', '2020-04-16', 'Online', '19:00', '22:00', '#FF9999', 13);",
                "insert into sem2 values (70, 'COMP7801', 'Topic in computer science', '2020-04-18', 'Online', '14:30', '17:30', '#76933C', 13);",
                "insert into sem2 values (71, 'COMP7904', 'Information security: attacks and defense', '2020-04-19', 'Online', '14:00', '17:00', '#B66DF3', 13);",
                "insert into sem2 values (72, 'COMP7407', 'Securities transaction banking', '2020-04-14', 'Recorded Lecture', '19:00', '22:00', '#FFFF00', 13);",
                "insert into sem2 values (73, 'COMP7405', 'Techniques in computational finance', '2020-04-15', 'Online', '19:00', '22:00', '#00B050', 13);",
                "insert into sem2 values (74, 'COMP7904', 'Information security: attacks and defense', '2020-04-16', 'Online', '19:00', '22:00', '#B66DF3', 13);",
                "insert into sem2 values (75, 'COMP7407', 'Securities transaction banking', '2020-04-17', 'Online', '19:00', '22:00', '#FFFF00', 13);",
                "insert into sem2 values (76, 'COMP7901', 'Legal protection of digital property', '2020-04-18', 'Online', '19:00', '22:00', '#0DF9F9', 13);",
                "insert into sem2 values (77, 'COMP7404D', 'Computational intelligence and machine learning', '2020-04-19', 'Online', '19:00', '22:00', '#FF9999', 13);",
                "insert into sem2 values (78, 'COMP7606A', 'Deep Learning', '2020-04-21', 'Online', '15:00', '18:00', '#F4DF42', 14);",
                "insert into sem2 values (79, 'COMP7606C', 'Deep Learning', '2020-04-20', 'Online', '19:00', '22:00', '#FABF8F', 14);",
                "insert into sem2 values (80, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-04-22', 'Online', '14:30', '17:30', '#CCECFF', 14);",
                "insert into sem2 values (81, 'COMP7404D', 'Computational intelligence and machine learning', '2020-04-23', 'Online', '19:00', '22:00', '#FF9999', 14);",
                "insert into sem2 values (82, 'COMP7801', 'Topic in computer science', '2020-04-25', 'Online', '14:30', '17:30', '#76933C', 14);",
                "insert into sem2 values (83, 'COMP7801', 'Topic in computer science', '2020-04-26', 'Online', '14:30', '17:30', '#76933C', 14);",
                "insert into sem2 values (84, 'COMP7901', 'Legal protection of digital property', '2020-04-20', 'Online', '19:00', '22:00', '#0DF9F9', 14);",
                "insert into sem2 values (85, 'COMP7506A', 'Smart phone apps development', '2020-04-21', 'Online', '19:00', '22:00', '#FF99FF', 14);",
                "insert into sem2 values (86, 'COMP7405', 'Techniques in computational finance', '2020-04-22', 'Online', '19:00', '22:00', '#00B050', 14);",
                "insert into sem2 values (87, 'COMP7904', 'Information security: attacks and defense', '2020-04-23', 'Online', '19:00', '22:00', '#B66DF3', 14);",
                "insert into sem2 values (88, 'COMP7606B', 'Deep Learning', '2020-04-24', 'Online', '19:00', '22:00', '#66FFCC', 14);",
                "insert into sem2 values (89, 'COMP7103B', 'Data mining', '2020-04-26', 'Recorded Lecture', '19:00', '22:00', '#00B0F0', 14);",
                "insert into sem2 values (90, 'COMP7606A', 'Deep Learning', '2020-04-28', 'Online', '15:00', '18:00', '#F4DF42', 15);",
                "insert into sem2 values (91, 'COMP7309', 'Quantum computing and artificial intelligence', '2020-04-29', 'Online', '14:30', '17:30', '#CCECFF', 15);",
                "insert into sem2 values (92, 'COMP7801', 'Topic in computer science', '2020-04-30', 'Online', '14:30', '17:30', '#76933C', 15);",
                "insert into sem2 values (93, 'COMP7901', 'Legal protection of digital property', '2020-04-27', 'Online', '19:00', '22:00', '#0DF9F9', 15);",
                "insert into sem2 values (94, 'COMP7506A', 'Smart phone apps development', '2020-04-28', 'Online', '19:00', '22:00', '#FF99FF', 15);",
                "insert into sem2 values (95, 'COMP7405', 'Techniques in computational finance', '2020-04-29', 'Online', '19:00', '22:00', '#00B050', 15);",
                "insert into sem2 values (96, 'COMP7606B', 'Deep Learning', '2020-04-30', 'Online', '19:00', '22:00', '#66FFCC', 15);",
                "insert into sem2 values (97, 'COMP7103B', 'Data mining', '2020-05-1', 'Recorded Lecture', '19:00', '22:00', '#00B0F0', 15);",
                "insert into sem2 values (98, 'COMP7506B', 'Smart phone apps development', '2020-05-7', 'Online', '15:30', '18:30', '#FF8409', 16);",
                "insert into sem2 values (99, 'COMP7404D', 'Computational intelligence and machine learning', '2020-05-6', 'Online', '19:00', '22:00', '#FF9999', 16);",
                "insert into sem2 values (100, 'COMP7404D', 'Computational intelligence and machine learning', '2020-05-7', 'Online', '19:00', '22:00', '#FF9999', 16);",
                "insert into sem2 values (101, 'COMP7801', 'Topic in computer science', '2020-05-9', 'Online', '14:30', '17:30', '#76933C', 16);",
                "insert into sem2 values (102, 'COMP7901', 'Legal protection of digital property', '2020-05-4', 'Online', '19:00', '22:00', '#0DF9F9', 16);",
                "insert into sem2 values (103, 'COMP7506A', 'Smart phone apps development', '2020-05-5', 'Online', '19:00', '22:00', '#FF99FF', 16);"
        };

        for (String insertion : insertions) {
            db.execSQL(insertion);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}