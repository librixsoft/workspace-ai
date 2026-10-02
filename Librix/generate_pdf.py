#!/usr/bin/env python3
"""Generate a single-page PDF from the LibrixSoft brochure HTML.
    
    Outputs exactly 1 page with no breaks, full height.
"""

import asyncio
import sys
from pathlib import Path

from playwright.async_api import async_playwright


async def generate_pdf(html_path: str, output_path: str) -> None:
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        page = await browser.new_page()

        html_file = Path(html_path).resolve()
        await page.goto(f"file://{html_file}", wait_until="networkidle")

        body_height = await page.evaluate("""() => {
            const html = document.documentElement;
            const body = document.body;
            return Math.max(
                html.scrollHeight,
                html.offsetHeight,
                body.offsetHeight,
                html.clientHeight,
                body.clientHeight
            );
        }""")

        page_width = 1280
        page_height = int(body_height) + 200

        await page.set_viewport_size({
            "width": page_width,
            "height": page_height
        })

        await page.emulate_media(media="screen")

        css_override = """
        @page {
            margin: 0;
            size: auto;
        }
        html {
            margin: 0 !important;
            padding: 0 !important;
            height: auto !important;
            width: 100% !important;
            background: #000 !important;
            overflow: hidden !important;
        }
        body {
            margin: 0 !important;
            padding: 0 !important;
            min-height: auto !important;
            height: auto !important;
            width: 100% !important;
            background: #000 !important;
            overflow: hidden !important;
            page-break-before: avoid !important;
            page-break-after: avoid !important;
            page-break-inside: avoid !important;
        }
        .brochure-section,
        .lx-hero,
        .overview-dashboard,
        .lx-carousel,
        .stats-row,
        .demo-mobile,
        .demo-cms,
        .demo-terminal,
        .demo-car-dash {
            page-break-before: avoid !important;
            page-break-after: avoid !important;
            page-break-inside: avoid !important;
        }
        .bg-blur, .bg-dots { display: none !important; }
        * {
            page-break-inside: avoid !important;
        }
        """
        await page.add_style_tag(content=css_override)

        # width/height in inches; prefer_css_page_size uses those over @page size
        await page.pdf(
            path=output_path,
            print_background=True,
            width=f"{page_width}px",
            height=f"{page_height}px",
            prefer_css_page_size=False,
            margin={"top": "0", "right": "0", "bottom": "0", "left": "0"},
        )

        await browser.close()

    print(f"[OK] PDF saved to {output_path}")


def main() -> None:
    script_dir = Path(__file__).parent.resolve()
    html_path = sys.argv[1] if len(sys.argv) > 1 else str(script_dir / "librixsoft_brochure_v2 2.html")
    output_path = sys.argv[2] if len(sys.argv) > 2 else str(script_dir / "librixsoft_brochure_v2.pdf")

    if not Path(html_path).exists():
        print(f"[ERROR] HTML file not found: {html_path}", file=sys.stderr)
        sys.exit(1)

    asyncio.run(generate_pdf(html_path, output_path))


if __name__ == "__main__":
    main()
