[![Build Status](https://travis-ci.com/yeastrc/proxl-import-xquest.svg?branch=master)](https://travis-ci.com/yeastrc/proxl-import-xquest)


XQuest to Proxl XML Converter
=====================================================

Use this program to convert the results of a XQuest cross-linking analysis to Proxl XML suitable for import into the proxl web application.

How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/proxl-import-xquest/releases).
2. Run the program ``java -jar xquest2ProxlXML.jar`` with no arguments to see the possible parameters.
3. Run the program, e.g., ``java -jar xquest2ProxlXML.jar -r ./xquest-output  -l dss  -o ./output.proxl.xml``

In the above example, ``output.proxl.xml`` will be created and be suitable for import into ProXL.

For more information on importing data into Proxl, please see the [Proxl Import Documentation](http://proxl-web-app.readthedocs.io/en/latest/using/upload_data.html).

More Information About Proxl
-----------------------------
For more information about Proxl, visit http://proxl-ms.org/.
