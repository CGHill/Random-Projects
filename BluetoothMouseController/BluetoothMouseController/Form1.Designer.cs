namespace BluetoothMouseController
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.listOutput = new System.Windows.Forms.ListBox();
            this.btnStart = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // listOutput
            // 
            this.listOutput.FormattingEnabled = true;
            this.listOutput.Location = new System.Drawing.Point(55, 29);
            this.listOutput.Name = "listOutput";
            this.listOutput.Size = new System.Drawing.Size(505, 264);
            this.listOutput.TabIndex = 0;
            // 
            // btnStart
            // 
            this.btnStart.Location = new System.Drawing.Point(586, 269);
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(89, 23);
            this.btnStart.TabIndex = 1;
            this.btnStart.Text = "Start";
            this.btnStart.UseVisualStyleBackColor = true;
            this.btnStart.Click += new System.EventHandler(this.btnStart_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(801, 506);
            this.Controls.Add(this.btnStart);
            this.Controls.Add(this.listOutput);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListBox listOutput;
        private System.Windows.Forms.Button btnStart;
    }
}

