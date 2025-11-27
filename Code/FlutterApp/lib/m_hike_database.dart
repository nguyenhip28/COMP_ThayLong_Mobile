import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class MHikeDatabase {
  static Database? _db;

  static Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await _initDB();
    return _db!;
  }

  static Future<Database> _initDB() async {
    final path = join(await getDatabasesPath(), 'mhike.db');
    return await openDatabase(path, version: 1, onCreate: (db, version) async {
      await db.execute('''
        CREATE TABLE hikes(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT,
          location TEXT,
          date TEXT,
          parking TEXT,
          length TEXT,
          difficulty TEXT,
          description TEXT,
          custom1 TEXT,
          custom2 TEXT
        )
      ''');
    });
  }

  static Future<void> saveHike(Map<String, dynamic> hike) async {
    final db = await database;
    await db.insert('hikes', hike);
  }

  static Future<List<Map<String, dynamic>>> getHikes() async {
    final db = await database;
    return await db.query('hikes', orderBy: 'date DESC');
  }

  static Future<void> deleteHike(int id) async {
    final db = await database;
    await db.delete('hikes', where: 'id = ?', whereArgs: [id]);
  }

  static Future<void> deleteAll() async {
    final db = await database;
    await db.delete('hikes');
  }
  static Future<void> updateHike(int id, Map<String, dynamic> hike) async {
    final db = await database;
    await db.update('hikes', hike, where: 'id = ?', whereArgs: [id]);
  }
}
