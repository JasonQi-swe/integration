package com.zijian.integration.sandbox;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;

public class TokenCalculator {
    public static void main(String[] args) {
                        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry(); //this is very very costly
               Encoding encoding = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
                int tokenCount = encoding.countTokens("About the job\n" +
                        "About Us\n" +
                        "\n" +
                        "Eagle Eye Networks is the global leader in cloud video surveillance, delivering cyber-secure, cloud-based video with artificial intelligence (AI) and analytics to make businesses more efficient and the world a safer place. The Eagle Eye Cloud VMS (video management system) is the only platform robust and flexible enough to power the future of video surveillance and intelligence. Eagle Eye is based in Austin, Texas, with offices in Amsterdam, Bangalore, and Tokyo. Learn more at een.com.\n" +
                        "\n" +
                        "\n" +
                        "Summary\n" +
                        "\n" +
                        "As Software Engineer (Java) you will be working on a daily basis expanding our video surveillance cloud Eagle Eye Camera Manager. From innovative new features for end-users to scaling our multi-cluster, high availability cloud to support the fast-growing amount of connected cameras. Your Java knowledge and experience will be used to architect, develop and release new code every day. Using continuous integration and bi-weekly Scrums we have a fast release cycle to bring your code to real customers.\n" +
                        "\n" +
                        "\n" +
                        "Responsibilities\n" +
                        "\n" +
                        "Development of features in Java\n" +
                        "Building test cases to test new or existing features\n" +
                        "Investigate and fix bugs\n" +
                        "Help colleagues with triaging issues\n" +
                        "\n" +
                        "\n" +
                        "Experience\n" +
                        "\n" +
                        "Technical Skills\n" +
                        "\n" +
                        "Excellent knowledge of JavaSE (11+);\n" +
                        "Strong database experience - SQL, PostgreSQL;\n" +
                        "Practical (work) experience with jOOQ, JUnit4/JUnit5, Spring MVC, Docker, Helm, K8s, Gradle, Flyway, IntelliJ, JSON, Git, Jira, REST APIs;\n" +
                        "Knowledge of and experience with microservice oriented architecture\n" +
                        "Knowledge of and experience with RT(S)P, H.264 and MP4, AAC and other audio/video codecs is an advantage;\n" +
                        "Personal Skills\n" +
                        "\n" +
                        "Fluent in English (writing + oral);\n" +
                        "The ability to quickly learn and adapt new programming concepts and a strong will to continuously improve development skills, and staying up to date with the latest technologies;\n" +
                        "The ability to identify technical requirements for the communication with other components and internal products and express them in a technical clear manner;\n" +
                        "Experience with design patterns, implementation, and usage of RESTful APIs or more low-level TCP based APIs;\n" +
                        "You are a technical, proactive, ambitious, fast learning person who is eager to bring awesome features to our growing customer base;\n" +
                        "Work & educational experience\n" +
                        "\n" +
                        "At least an academic level in a Computer Science field;\n" +
                        "At least 3-5 years of continuous, strong and practical Java (back-end) development experience, most of which in a professional organization (for med/senior positions);\n" +
                        "You have been working in an Agile environment using Scrum methodology;\n" +
                        "\n" +
                        "\n" +
                        "What can you expect from us?\n" +
                        "\n" +
                        "Diverse team with 25+ nationalities\n" +
                        "23 paid holidays yearly + Dutch public holidays\n" +
                        "Compensation for travel expenses from your home to the office - in case the distance is more than 10 kilometers - up to € 200,- per month;\n" +
                        "Company equipment for work use;\n" +
                        "Enrollment in the Eagle Eye Networks B.V. collective pension plan\n" +
                        "Drinks & Snacks at the AMS office\n" +
                        "Opportunity to take part in EEN Corporate Fitness App challenges\n" +
                        "Company swag, business casual scale-up work atmosphere and Friday drinks\n" +
                        "Once or twice a year a stunning company day\n" +
                        "Flexible work time arrangement\n" +
                        "\n" +
                        "\n" +
                        "More About Eagle Eye Networks\n" +
                        "\n" +
                        "Eagle Eye Networks is leveraging artificial intelligence on its true cloud platform to dramatically reshape the video surveillance and security industry. The Eagle Eye Cloud Video Management System (VMS) is a smart cloud video surveillance solution, purpose-built to help businesses improve safety, security, operations, and customer service. Tens of thousands of companies in more than 90 countries around the globe have moved their video surveillance to the cloud with Eagle Eye VMS. Customers, including multi-family residences, smart cities, schools, hospitals, hotels, logistics, restaurants, and retail shops trust Eagle Eye for actionable business intelligence and proactive security across multiple locations. The Eagle Eye VMS has strong APIs for the secure integration of third-party systems and works with thousands of industry cameras, so customers don’t have to “rip and replace” their existing infrastructure. Eagle Eye Cloud VMS is the only platform robust enough to power the future of video surveillance.");
        System.out.println("tokenCount: "+tokenCount);
    }
}
