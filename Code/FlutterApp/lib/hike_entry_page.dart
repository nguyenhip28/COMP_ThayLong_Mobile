import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'm_hike_database.dart';

class HikeEntryPage extends StatefulWidget {
  final Map<String, dynamic>? hikeToEdit;
  const HikeEntryPage({super.key, this.hikeToEdit});

  @override
  State<HikeEntryPage> createState() => _HikeEntryPageState();
}

class _HikeEntryPageState extends State<HikeEntryPage> {
  final _formKey = GlobalKey<FormState>();

  final TextEditingController nameController = TextEditingController();
  final TextEditingController locationController = TextEditingController();
  final TextEditingController lengthController = TextEditingController();
  final TextEditingController difficultyController = TextEditingController();
  final TextEditingController descriptionController = TextEditingController();
  final TextEditingController custom1Controller = TextEditingController();
  final TextEditingController custom2Controller = TextEditingController();

  String? parking;
  DateTime hikeDate = DateTime.now();

  @override
  void initState() {
    super.initState();
    if (widget.hikeToEdit != null) {
      final hike = widget.hikeToEdit!;
      nameController.text = hike['name'];
      locationController.text = hike['location'];
      lengthController.text = hike['length'];
      difficultyController.text = hike['difficulty'];
      descriptionController.text = hike['description'] ?? '';
      custom1Controller.text = hike['custom1'] ?? '';
      custom2Controller.text = hike['custom2'] ?? '';
      parking = hike['parking'];
      hikeDate = DateTime.parse(hike['date']);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(widget.hikeToEdit != null ? 'Edit Hike' : 'Enter Hike Details')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: nameController,
                decoration: const InputDecoration(labelText: 'Hike Name *'),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              TextFormField(
                controller: locationController,
                decoration: const InputDecoration(labelText: 'Location *'),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              ListTile(
                title: Text('Date: ${DateFormat('yyyy-MM-dd').format(hikeDate)}'),
                trailing: const Icon(Icons.calendar_today),
                onTap: _pickDate,
              ),
              DropdownButtonFormField<String>(
                value: parking,
                hint: const Text('Parking Available *'),
                items: const ['Yes', 'No'].map((e) => DropdownMenuItem(value: e, child: Text(e))).toList(),
                onChanged: (val) => setState(() => parking = val),
                validator: (value) => value == null ? 'Required' : null,
              ),
              TextFormField(
                controller: lengthController,
                decoration: const InputDecoration(labelText: 'Length *'),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              TextFormField(
                controller: difficultyController,
                decoration: const InputDecoration(labelText: 'Difficulty *'),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              TextFormField(
                controller: descriptionController,
                decoration: const InputDecoration(labelText: 'Description'),
              ),
              TextFormField(
                controller: custom1Controller,
                decoration: const InputDecoration(labelText: 'Custom Field 1'),
              ),
              TextFormField(
                controller: custom2Controller,
                decoration: const InputDecoration(labelText: 'Custom Field 2'),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submitHike,
                child: Text(widget.hikeToEdit != null ? 'Update Hike' : 'Submit'),
              )
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _pickDate() async {
    DateTime? date = await showDatePicker(
      context: context,
      initialDate: hikeDate,
      firstDate: DateTime(2000),
      lastDate: DateTime(2100),
    );
    if (date != null) setState(() => hikeDate = date);
  }

  Future<void> _submitHike() async {
    if (_formKey.currentState!.validate() && parking != null) {
      bool confirmed = await showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: const Text('Confirm Hike'),
          content: Text(
            'Name: ${nameController.text}\n'
                'Location: ${locationController.text}\n'
                'Date: ${DateFormat('yyyy-MM-dd').format(hikeDate)}\n'
                'Parking: $parking\n'
                'Length: ${lengthController.text}\n'
                'Difficulty: ${difficultyController.text}\n'
                'Description: ${descriptionController.text}\n'
                'Custom1: ${custom1Controller.text}\n'
                'Custom2: ${custom2Controller.text}',
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Edit')),
            ElevatedButton(onPressed: () => Navigator.pop(context, true), child: const Text('Confirm')),
          ],
        ),
      );

      if (confirmed) {
        Map<String, dynamic> hikeData = {
          'name': nameController.text,
          'location': locationController.text,
          'date': hikeDate.toIso8601String(),
          'parking': parking!,
          'length': lengthController.text,
          'difficulty': difficultyController.text,
          'description': descriptionController.text,
          'custom1': custom1Controller.text,
          'custom2': custom2Controller.text,
        };

        if (widget.hikeToEdit != null) {
          await MHikeDatabase.updateHike(widget.hikeToEdit!['id'], hikeData);
          if (!mounted) return;
          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Hike updated!')));
        } else {
          await MHikeDatabase.saveHike(hikeData);
          if (!mounted) return;
          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Hike saved!')));
        }

        Navigator.pop(context, true);
      }
    }
  }
}
