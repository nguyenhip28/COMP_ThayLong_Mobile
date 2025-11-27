import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'hike_entry_page.dart';

class HikeDetailPage extends StatelessWidget {
  final Map<String, dynamic> hike;
  const HikeDetailPage({super.key, required this.hike});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(hike['name']),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: () async {
              bool? updated = await Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => HikeEntryPage(hikeToEdit: hike)),
              );
              if (updated == true && context.mounted) {
                Navigator.pop(context, true); // trả về true cho HikeListPage reload
              }
            },
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            Text("Name: ${hike['name']}", style: const TextStyle(fontSize: 18)),
            Text("Location: ${hike['location']}", style: const TextStyle(fontSize: 18)),
            Text("Date: ${DateFormat('yyyy-MM-dd').format(DateTime.parse(hike['date']))}", style: const TextStyle(fontSize: 18)),
            Text("Parking: ${hike['parking']}", style: const TextStyle(fontSize: 18)),
            Text("Length: ${hike['length']}", style: const TextStyle(fontSize: 18)),
            Text("Difficulty: ${hike['difficulty']}", style: const TextStyle(fontSize: 18)),
            Text("Description: ${hike['description']}", style: const TextStyle(fontSize: 18)),
            Text("Custom Field 1: ${hike['custom1']}", style: const TextStyle(fontSize: 18)),
            Text("Custom Field 2: ${hike['custom2']}", style: const TextStyle(fontSize: 18)),
          ],
        ),
      ),
    );
  }
}
