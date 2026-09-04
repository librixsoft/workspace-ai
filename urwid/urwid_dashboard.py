#!/usr/bin/env python3
"""
Urwid Dashboard - Interface Only (No Functionality)
This file contains the complete UI structure without any functionality.
"""

import urwid

def create_dashboard():
    """Create a comprehensive dashboard layout."""
    
    # Header section  
    header_text = urwid.Text("Dashboard", align='center')
    header_divider = urwid.Divider()
    
    # Main content area with placeholder widgets
    grid_content = urwid.Pile([
        urwid.Text("[Widget 1] - Stats Overview"),
        urwid.Divider(),
        urwid.Text("[Widget 2] - Recent Activity"), 
        urwid.Divider(),
        urwid.Text("[Widget 3] - Quick Actions"),
    ])
    
    # Left sidebar menu items
    sidebar_items = [
        "Dashboard",
        "Settings", 
        "Profile",
        "Reports",
        "Analytics",
        "Users",
        "System"
    ]
    
    sidebar_widgets = []
    for item in sidebar_items:
        sidebar_widgets.append(urwid.Button(item))
        sidebar_widgets.append(urwid.Divider())
    
    sidebar_list = urwid.Pile(sidebar_widgets)
    sidebar = urwid.ListBox([sidebar_list])
    
    # Right panel with quick info
    right_panel = urwid.Pile([
        urwid.Text("Quick Info"),
        urwid.Divider(),
        urwid.Text("Status: Online"),
        urwid.Divider(),
        urwid.Text("Last Update: Now"),
        urwid.Divider(),
        urwid.Text("[Widget 4] - Notifications"),
    ])
    
    # Footer status bar
    footer_text = urwid.Text("Ready - [Ctrl+C to exit]", align='center')
    footer_divider = urwid.Divider()
    
    # Create main layout using Columns for central area
    content_columns = urwid.Columns([
        ('weight', 1, grid_content),
    ], dividechars=1)
    
    # Main columns with sidebar and content  
    main_columns = urwid.Columns([
        ('fixed', 20, sidebar),
        ('weight', 1, content_columns),
        ('fixed', 20, right_panel)
    ], dividechars=1)
    
    # Final layout with header and footer
    final_layout = urwid.Pile([
        header_text,
        header_divider,
        main_columns,
        footer_divider,
        footer_text
    ])
    
    return final_layout

def main():
    """Main function - creates and runs the dashboard."""
    try:
        # Create the dashboard interface
        dashboard = create_dashboard()
        
        # Set up palette for styling
        palette = [
            ('header', 'white', 'dark blue'),
            ('footer', 'light gray', 'dark blue'),
            ('button', 'black', 'light gray'),
            ('button_focus', 'white', 'dark gray'),
            ('widget_header', 'white', 'dark cyan'),
            ('status_online', 'light green', 'default'),
            ('divider', 'dark gray', 'default')
        ]
        
        print("Dashboard interface created successfully.")
        print("Starting interactive dashboard...")
        print("Press Ctrl+C to exit")
        
        # Run the main loop
        loop = urwid.MainLoop(dashboard, palette)
        loop.run()
        
    except KeyboardInterrupt:
        print("\nDashboard exited by user request.")
    except Exception as e:
        print(f"Error running dashboard: {e}")
        import traceback
        traceback.print_exc()
        return 1
    
    return 0

if __name__ == "__main__":
    main()