XQuest to ProXL XML Converter
=====================================================

Use this program to convert the results of a XQuest cross-linking analysis to ProXL XML suitable for import into the ProXL web application.

How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/proxl-import-xquest/releases).
2. Run the program ``java -jar xquest2ProxlXML.jar`` with no arguments to see the possible parameters.
3. Run the program, e.g., ``java -jar xquest2ProxlXML.jar -r ./xquest-output  -l dss  -o ./output.proxl.xml``

In the above example, ``output.proxl.xml`` will be created and be suitable for import into ProXL.

For more information on importing data into ProXL, please see the [ProXL Import Documentation](http://proxl-web-app.readthedocs.org/en/latest/install/import.html).
