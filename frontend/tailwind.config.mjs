/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./components/**/*.{js,ts,jsx,tsx,mdx}",
        "./*.{js,ts,jsx,tsx,mdx}",
    ],
    theme: {
        extend: {
            colors: {
                background: "var(--background)",
                foreground: "var(--foreground)",
            },
        },
    },
    plugins: [],
};
