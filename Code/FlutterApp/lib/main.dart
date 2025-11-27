import 'package:flutter/material.dart';
import 'hike_entry_page.dart';
import 'hike_list_page.dart';

void main() {
  runApp(const MHikeApp());
}

class MHikeApp extends StatelessWidget {
  const MHikeApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'M-Hike',
      theme: ThemeData(
        primarySwatch: Colors.green,
        useMaterial3: true,
      ),
      home: const HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('M-Hike Home')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => const HikeEntryPage()),
                );
              },
              child: const Text('Enter New Hike'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => const HikeListPage()),
                );
              },
              child: const Text('View Hikes List'),
            ),
          ],
        ),
      ),
    );
  }
}
