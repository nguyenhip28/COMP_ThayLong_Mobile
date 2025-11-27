import 'package:flutter/material.dart';
import 'hike_detail_page.dart';
import 'm_hike_database.dart';

class HikeListPage extends StatefulWidget {
  const HikeListPage({super.key});

  @override
  State<HikeListPage> createState() => _HikeListPageState();
}

class _HikeListPageState extends State<HikeListPage> {
  List<Map<String, dynamic>> hikes = [];

  @override
  void initState() {
    super.initState();
    _loadHikes();
  }

  Future<void> _loadHikes() async {
    hikes = await MHikeDatabase.getHikes();
    if (mounted) setState(() {});
  }

  Future<void> _deleteHike(int id) async {
    await MHikeDatabase.deleteHike(id);
    await _loadHikes();
  }

  Future<void> _deleteAll() async {
    bool confirm = await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete All Hikes?'),
        content: const Text('This will delete all hikes permanently.'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancel')),
          ElevatedButton(onPressed: () => Navigator.pop(context, true), child: const Text('Delete All')),
        ],
      ),
    );

    if (confirm) {
      await MHikeDatabase.deleteAll();
      await _loadHikes();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Hikes List'),
        actions: [IconButton(onPressed: _deleteAll, icon: const Icon(Icons.delete_forever))],
      ),
      body: hikes.isEmpty
          ? const Center(child: Text('No hikes available'))
          : ListView.builder(
        itemCount: hikes.length,
        itemBuilder: (context, index) {
          final hike = hikes[index];
          return Card(
            margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            child: ListTile(
              title: Text(hike['name'], style: const TextStyle(fontWeight: FontWeight.bold)),
              subtitle: Text(hike['location']),
              onTap: () async {
                bool? updated = await Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => HikeDetailPage(hike: hike)),
                );
                if (updated == true) await _loadHikes();
              },
              trailing: IconButton(
                icon: const Icon(Icons.delete, color: Colors.red),
                onPressed: () async {
                  bool confirm = await showDialog(
                    context: context,
                    builder: (context) => AlertDialog(
                      title: const Text('Delete Hike?'),
                      content: Text('Are you sure you want to delete "${hike['name']}"?'),
                      actions: [
                        TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancel')),
                        ElevatedButton(onPressed: () => Navigator.pop(context, true), child: const Text('Delete')),
                      ],
                    ),
                  );
                  if (confirm) await _deleteHike(hike['id']);
                },
              ),
            ),
          );
        },
      ),
    );
  }
}
