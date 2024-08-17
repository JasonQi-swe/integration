package com.example.integration.sandbox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class parseDescription {

    public static void main(String[] args) {
        String html = """
                <article class="jobs-description__container
                          jobs-description__container--condensed">
                        <div class="jobs-description__content jobs-description-content
                            jobs-description__content--condensed">
                          <div class="jobs-box__html-content jobs-description-content__text t-14 t-normal
                              jobs-description-content__text--stretch" id="job-details" tabindex="-1">
                            <h2 class="text-heading-large">
                              About the job
                            </h2>
                                
                <!---->            <div class="mt4">
                                <span><p><!---->Java Backend Developer<!----></p></span><span><p><!---->Fully Remote<!----></p></span><span><p><!---->9 Months<!----></p></span><span><p><!---->Inside IR35<!----></p></span><span><p><!---->Paying up to £285 a day<!----></p></span><span><p><!---->Key Skills:<!----></p></span><span><p><!---->• Java Backend Development<!----></p></span><span><p><!---->• Infrastructure Automation<!----></p></span><span><p><!---->• Amazon Web Services or other cloud providers<!----></p></span><span><p><!---->• Networking<!----></p></span><span><p><!---->• DevOps Experience<!----></p></span><span><p><span><br></span></p></span><span><p><!---->We’re looking for Software Development Engineers to work with experienced teams in Berlin, and deliver on our next generation of product and services from the ground up. We’re looking for a passionate, team-oriented, and talented engineer who has experience building innovative, sophisticated applications that customers love.<!----></p></span><span><p><span><br></span></p></span><span><p><!---->You’ll work to design and implement cutting edge products and services used every day, by people you know. You’ll be involved in shipping full stack applications dealing with modern front-end framework, cloud based services, workflow architecture and Machine Learning. You’ll need a strong sense of ownership, bias for action, agility, and creativity as well as a focus on the technical operations for your team’s systems. If you enjoy solving complex problems and tackling tough challenges, we’d love to hear from you!<!----></p></span><span><p><span><br></span></p></span><span><p><span><br></span></p></span><span><p><!---->Deliverables<!----></p></span><span><p><!---->To comply with this law, the Reviews front-end teams have 490 issues to fix. Out of these 490, we believe that about 5 are considered “hard”, i.e., require significant engineering effort or coordination across multiple services. The responsibilities of the third-party vendor staff will be to own 485 “easy” / “medium” fixes, involving:<!----></p></span><span><p><!---->• Ramping up on accessibility and services to be remediated<!----></p></span><span><p><!---->• Working with the POC to resolve all ambiguities with the triaged tickets: When accessibility defects are identified in shared components, employees should notify POCs to advocate for resolutions from AUI teams.<!----></p></span><span><p><!---->• Signing up for Wa11y Q&amp;A sessions in Accessibility office hours when they encounter technical challenges<!----></p></span><span><p><!---->• Fixing the accessibility tickets which includes changing the code, creating any string translation requests as necessary, testing it &amp; creating code reviews.<!----></p></span><span><p><!---->• Ensuring code reviews are merged and after they reach prod, signing up for Wa11y Accessibility office hours for Play With Sessions (PWS), to ensure all issues have been fixed. If new issues have been identified, they will also be responsible to re-iterate and fix the issues.<!----></p></span><span><p><!---->• If an issue can’t be fixed, they will need to discuss with the team and request an exception.<!----></p></span><span><p><span><br></span></p></span><span><p><!---->Skills<!----></p></span><span><p><!---->The employees working on the accessibility tickets will need to have knowledge about the following:<!----></p></span><span><p><!---->• Frontend technologies : Javascript, Typescript, HTML, CSS, JQuery, JSP, NodeJS, NPM<!----></p></span><span><p><!---->• Strong onboarding skills and ability to rapidly understand new codebases<!----></p></span><span><p><!---->• Good to have but not necessary : prior experiences leveraging web accessibility features, performing keyboard navigation testing, and working with screen readers<!----></p></span><span><p><span><br></span></p></span><span><p><!---->Deliverables<!----></p></span><span><p><!---->For every service, we consider the task accomplished if all of the following acceptance criteria are met.<!----></p></span><span><p><!---->1. Service is migrated to Java Development Kit (JDK) 17 and AWS 2.0 SDK.<!----></p></span><span><p><!---->2. Service is optimized for AWS Graviton (building locally for ARM).<!----></p></span><span><p><!---->3. AWS infrastructure creation is fully automated through CDK (Cloud Development Kit). Contractors are not expect (at least initially) to develop net-new CDK code, but rather extend existing ones.<!----></p></span><span><p><!---->4. AWS services not available in the target region are replaced by alternative AWS services<!----></p></span><span><p><!---->5. Sentry (internal single-sign-on technology on deprecation path) is not used anymore<!----></p></span><span><p><!---->6. Load balancers are migrated from NetScaler to Tardigrade (internal technologies based on AWS)<!----></p></span><span><p><!---->7. Service is spun up in AWS Zaragoza Region and all pipeline tests are executed and pass<!----></p></span><span><p><span><br></span></p></span><span><p><!---->Skills<!----></p></span><span><p><!---->Candidates are expected to have 2 years of experience in 4 out of the 5 following domains.<!----></p></span><span><p><!---->• Java Backend Development<!----></p></span><span><p><!---->• Infrastructure Automation<!----></p></span><span><p><!---->• AWS or other cloud providers<!----></p></span><span><p><!---->• Networking<!----></p></span><span><p><!---->• DevOps Experience<!----></p></span><span><p><span><br></span></p></span><span><p><!---->If you are interested please apply or send your CV to Luke.sandilands@cpl.com<!----></p></span>
                <!---->            </div>
                          </div>
                          <div class="jobs-description__details">
                <!---->          </div>
                        </div>
                      </article>
                """;

        Document doc = Jsoup.parse(html);

        Elements contentElements = doc.select("article.jobs-description__container");

        for (Element element : contentElements) {
            Elements spans = element.select("span");
            for (Element span : spans) {
                System.out.println(span.text());
            }
        }
    }
}

