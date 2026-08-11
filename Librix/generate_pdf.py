#!/usr/bin/env python3
from weasyprint import HTML, CSS
import os

html_path = os.path.join(os.path.dirname(__file__), 'Enterprise_Services_LibrixSoft.html')
pdf_path = os.path.join(os.path.dirname(__file__), 'Enterprise_Services_LibrixSoft.pdf')

# A4 page with margins that match the container padding in HTML
css_text = """
@page {
  size: A4;
  margin: 15mm;
}
"""

html = HTML(filename=html_path)
html.write_pdf(pdf_path, stylesheets=[CSS(string=css_text)], presentational_hints=True)
print(f"PDF generated: {pdf_path}")
