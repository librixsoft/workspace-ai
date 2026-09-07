from playwright.sync_api import sync_playwright

TARGET_URL = "https://example.com"

DEFAULT_USER_AGENT = (
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
    "AppleWebKit/537.36 (KHTML, like Gecko) "
    "Chrome/127.0.0.0 Safari/537.36"
)

EXTRA_HEADERS = {
    "Accept-Language": "en-US,en;q=0.9",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
}

INIT_SCRIPT = """
Object.defineProperty(navigator, 'webdriver', {get: () => undefined});
Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3, 4, 5]});
Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']});
window.chrome = {runtime: {}};
"""


def main():
    print("=" * 60)
    print("  CVE-2024-4666 - Cloudflare Bypass with Playwright")
    print(f"  Target: {TARGET_URL}")
    print("=" * 60)

    with sync_playwright() as pw:
        browser = pw.chromium.launch(
            headless=True,
            args=[
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-blink-features=AutomationControlled",
                "--disable-extensions",
                "--window-size=1920,1080",
            ],
        )

        context = browser.new_context(
            viewport={"width": 1920, "height": 1080},
            user_agent=DEFAULT_USER_AGENT,
            locale="en-US",
            timezone_id="America/Mexico_City",
            extra_http_headers=EXTRA_HEADERS,
        )

        context.add_init_script(INIT_SCRIPT)

        page = context.new_page()

        print(f"\n  [*] Navigating to {TARGET_URL}...")
        page.goto(TARGET_URL, timeout=30000, wait_until="domcontentloaded")
        page.wait_for_selector("body", timeout=30000)
        page.wait_for_timeout(3000)

        print("  [+] Page loaded successfully")
        print(f"  [+] Title: {page.title()}")
        print(f"  [+] URL: {page.url}")
        print(f"  [+] Response length: {len(page.content())} chars")

        page.close()
        context.close()
        browser.close()

        print("\n  [*] Done.")


if __name__ == "__main__":
    main()
