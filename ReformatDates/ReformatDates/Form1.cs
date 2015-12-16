using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace ReformatDates
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            string[] lines = System.IO.File.ReadAllLines(@"D:\incidentData_07350817092015.txt");
            StreamWriter sw = new StreamWriter(@"D:\newFile.txt");

            // Display the file contents by using a foreach loop.
            foreach (string line in lines)
            {
                string[] commaSeparatedValues = line.Split(',');

                foreach(string value in commaSeparatedValues)
                {
                    if (value.Contains("/"))
                    {
                        //replace
                        string[] dateSplit = value.Split('/');

                        string day = dateSplit[0];
                        string month = dateSplit[1];
                        string year = dateSplit[2];

                        string newDateFormat = year + "-" + month + "-" + day;
                        sw.Write(newDateFormat);
                        sw.Write(",");
                    }
                    else
                    {
                        sw.Write(value);
                        sw.Write(",");
                        //Normal value, write to file
                    }
                }

                sw.Close();
                // Use a tab to indent each line of the file.
                Console.WriteLine("\t" + line);
            }
        }
    }
}
