package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DummyDataService {

    @Autowired private UserRepository userRepository;
    @Autowired private ContactInfoRepository contactInfoRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private PortfolioRepository portfolioRepository;
    @Autowired private TestimonialRepository testimonialRepository;
    @Autowired private BlogRepository blogRepository;
    @Autowired private AchievementRepository achievementRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private PricingPlanRepository pricingPlanRepository;
    @Autowired private FAQRepository faqRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    public void createCompleteDummyData() {
        // Create main user (simplified)
        User user = createDummyUser();
        UUID userId = user.getId();

        // Create all related data
        createDummyContactInfo(userId);
        createDummyServices(userId);
        createDummyPortfolios(userId);
        createDummyTestimonials(userId);
        createDummyBlogs(userId);
        createDummyAchievements(userId);
        createDummyTeamMembers(userId);
        createDummyEvents(userId);
        createDummyPricingPlans(userId);
        createDummyFAQs(userId);
        createDummyAppointments(userId);
    }

    private User createDummyUser() {
        User user = new User();
        user.setName("John Doe");
        user.setTitle("Full Stack Developer & UI/UX Designer");
        user.setDescription("Passionate developer with 8+ years of experience in creating beautiful and functional web applications. Specialized in React, Node.js, and modern web technologies. Expert in building scalable solutions and delivering exceptional user experiences.");

        return userRepository.save(user);
    }

    private void createDummyContactInfo(UUID userId) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUserId(userId);
        contactInfo.setEmail("john.doe@example.com");
        contactInfo.setPhone("+1 (555) 123-4567");
        contactInfo.setAddress("123 Tech Street, San Francisco, CA 94105");
        contactInfo.setWebsite("https://johndoe.dev");
        contactInfo.setSocialLinks("{\"linkedin\":\"https://linkedin.com/in/johndoe\",\"twitter\":\"https://twitter.com/johndoe\",\"github\":\"https://github.com/johndoe\",\"facebook\":\"https://facebook.com/johndoe\",\"instagram\":\"https://instagram.com/johndoe\",\"dribbble\":\"https://dribbble.com/johndoe\"}");
        contactInfo.setContactFormEnabled(true);
        contactInfo.setPreferredContactMethod("email");
        contactInfo.setAvailabilityHours("Monday - Friday, 9:00 AM - 6:00 PM PST");
        contactInfo.setTimeZone("America/Los_Angeles");

        contactInfoRepository.save(contactInfo);
    }

    private void createDummyServices(UUID userId) {
        try {
            List<com.example.backend.entity.Service> services = new ArrayList<>();

            // Web Development Service
            com.example.backend.entity.Service webDev = new com.example.backend.entity.Service();
            webDev.setUserId(userId);
            webDev.setTitle("Full Stack Web Development");
            webDev.setDescription("Complete web application development from frontend to backend using modern technologies like React, Node.js, and MongoDB.");
            webDev.setPrice(new BigDecimal("5000.00"));
            webDev.setDuration("4-8 weeks");
            webDev.setCategory("Web Development");
            webDev.setImageUrl("https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=600");
            webDev.setFeatures("[\"Responsive Design\",\"Database Integration\",\"API Development\",\"Testing\",\"Deployment\"]");
            webDev.setServiceType("one-time");
            webDev.setConsultationRequired(true);
            webDev.setIsActive(true);
            webDev.setSortOrder(1);
            services.add(webDev);

            // UI/UX Design Service
            com.example.backend.entity.Service uiux = new com.example.backend.entity.Service();
            uiux.setUserId(userId);
            uiux.setTitle("UI/UX Design");
            uiux.setDescription("User-centered design solutions including wireframing, prototyping, and visual design for web and mobile applications.");
            uiux.setPrice(new BigDecimal("3000.00"));
            uiux.setDuration("3-5 weeks");
            uiux.setCategory("Design");
            uiux.setImageUrl("https://images.unsplash.com/photo-1561070791-2526d30994b5?w=600");
            uiux.setFeatures("[\"Wireframing\",\"Prototyping\",\"Visual Design\",\"User Testing\",\"Design System\"]");
            uiux.setServiceType("one-time");
            uiux.setConsultationRequired(false);
            uiux.setIsActive(true);
            uiux.setSortOrder(2);
            services.add(uiux);

            // Technical Consulting Service
            com.example.backend.entity.Service consulting = new com.example.backend.entity.Service();
            consulting.setUserId(userId);
            consulting.setTitle("Technical Consulting");
            consulting.setDescription("Expert technical guidance for architecture decisions, technology stack selection, and project planning.");
            consulting.setPrice(new BigDecimal("150.00"));
            consulting.setDuration("Per hour");
            consulting.setCategory("Consulting");
            consulting.setImageUrl("https://images.unsplash.com/photo-1600880292203-757bb62b4baf?w=600");
            consulting.setFeatures("[\"Architecture Review\",\"Code Review\",\"Performance Optimization\",\"Technology Selection\"]");
            consulting.setServiceType("recurring");
            consulting.setConsultationRequired(true);
            consulting.setIsActive(true);
            consulting.setSortOrder(3);
            services.add(consulting);

            // Save all services
            serviceRepository.saveAll(services);

        } catch (Exception e) {
            System.err.println("Error creating dummy services: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createDummyPortfolios(UUID userId) {
        List<Portfolio> portfolios = new ArrayList<>();

        Portfolio ecommerce = new Portfolio();
        ecommerce.setUserId(userId);
        ecommerce.setTitle("E-commerce Platform");
        ecommerce.setDescription("A modern e-commerce platform built with React and Node.js featuring payment integration, inventory management, and admin dashboard.");
        ecommerce.setCategory("Web Development");
        ecommerce.setClientName("TechStore Inc.");
        ecommerce.setProjectDate(LocalDate.of(2023, 6, 15));
        ecommerce.setProjectUrl("https://techstore-demo.com");
        ecommerce.setGithubUrl("https://github.com/johndoe/ecommerce-platform");
        ecommerce.setImageUrls("[\"https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800\",\"https://images.unsplash.com/photo-1556742111-a301076d9d18?w=800\"]");
        ecommerce.setTechnologies("[\"React\",\"Node.js\",\"MongoDB\",\"Stripe API\",\"Express.js\"]");
        ecommerce.setIsFeatured(true);
        ecommerce.setStatus("completed");
        ecommerce.setCompletionPercentage(100);
        ecommerce.setTestimonial("Outstanding work! The platform exceeded our expectations and significantly boosted our online sales.");
        ecommerce.setTestimonialAuthor("Sarah Johnson, CEO at TechStore Inc.");
        ecommerce.setTags("[\"E-commerce\",\"Full Stack\",\"React\",\"Payment Integration\"]");
        ecommerce.setSortOrder(1);
        portfolios.add(ecommerce);

        Portfolio dashboard = new Portfolio();
        dashboard.setUserId(userId);
        dashboard.setTitle("Analytics Dashboard");
        dashboard.setDescription("Real-time analytics dashboard with interactive charts and data visualization for business intelligence.");
        dashboard.setCategory("Data Visualization");
        dashboard.setClientName("DataCorp");
        dashboard.setProjectDate(LocalDate.of(2023, 9, 10));
        dashboard.setProjectUrl("https://analytics-demo.com");
        dashboard.setImageUrls("[\"https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800\",\"https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800\"]");
        dashboard.setTechnologies("[\"React\",\"D3.js\",\"Python\",\"PostgreSQL\",\"Chart.js\"]");
        dashboard.setStatus("completed");
        dashboard.setCompletionPercentage(100);
        dashboard.setSortOrder(2);
        portfolios.add(dashboard);

        Portfolio mobileApp = new Portfolio();
        mobileApp.setUserId(userId);
        mobileApp.setTitle("Task Management Mobile App");
        mobileApp.setDescription("Cross-platform mobile application for task and project management with real-time collaboration features.");
        mobileApp.setCategory("Mobile Development");
        mobileApp.setClientName("ProductivityPro");
        mobileApp.setProjectDate(LocalDate.of(2024, 1, 20));
        mobileApp.setProjectUrl("https://taskapp-demo.com");
        mobileApp.setImageUrls("[\"https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=800\"]");
        mobileApp.setTechnologies("[\"React Native\",\"Firebase\",\"Redux\",\"TypeScript\"]");
        mobileApp.setStatus("completed");
        mobileApp.setCompletionPercentage(100);
        mobileApp.setSortOrder(3);
        portfolios.add(mobileApp);

        portfolioRepository.saveAll(portfolios);
    }

    private void createDummyTestimonials(UUID userId) {
        List<Testimonial> testimonials = new ArrayList<>();

        Testimonial testimonial1 = new Testimonial();
        testimonial1.setUserId(userId);
        testimonial1.setClientName("Sarah Johnson");
        testimonial1.setClientTitle("CEO");
        testimonial1.setClientCompany("TechStore Inc.");
        testimonial1.setTestimonialText("John delivered an exceptional e-commerce platform that transformed our business. His attention to detail and technical expertise are outstanding. The platform is fast, user-friendly, and has significantly increased our conversion rates.");
        testimonial1.setClientImageUrl("https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150");
        testimonial1.setRating(5);
        testimonial1.setProjectName("E-commerce Platform");
        testimonial1.setTestimonialDate(LocalDate.of(2023, 7, 1));
        testimonial1.setIsFeatured(true);
        testimonial1.setIsApproved(true);
        testimonial1.setClientWebsite("https://techstore.com");
        testimonial1.setSortOrder(1);
        testimonials.add(testimonial1);

        Testimonial testimonial2 = new Testimonial();
        testimonial2.setUserId(userId);
        testimonial2.setClientName("Michael Chen");
        testimonial2.setClientTitle("Product Manager");
        testimonial2.setClientCompany("DataCorp");
        testimonial2.setTestimonialText("The analytics dashboard John created for us is simply amazing. It provides real-time insights that have helped us make better business decisions. Professional, reliable, and highly skilled developer.");
        testimonial2.setClientImageUrl("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150");
        testimonial2.setRating(5);
        testimonial2.setProjectName("Analytics Dashboard");
        testimonial2.setTestimonialDate(LocalDate.of(2023, 10, 15));
        testimonial2.setIsFeatured(true);
        testimonial2.setIsApproved(true);
        testimonial2.setSortOrder(2);
        testimonials.add(testimonial2);

        Testimonial testimonial3 = new Testimonial();
        testimonial3.setUserId(userId);
        testimonial3.setClientName("Emily Rodriguez");
        testimonial3.setClientTitle("Startup Founder");
        testimonial3.setClientCompany("ProductivityPro");
        testimonial3.setTestimonialText("Working with John was a fantastic experience. He understood our vision perfectly and delivered a mobile app that exceeded our expectations. Great communication and technical skills!");
        testimonial3.setClientImageUrl("https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150");
        testimonial3.setRating(5);
        testimonial3.setProjectName("Task Management Mobile App");
        testimonial3.setTestimonialDate(LocalDate.of(2024, 2, 28));
        testimonial3.setIsApproved(true);
        testimonial3.setSortOrder(3);
        testimonials.add(testimonial3);

        testimonialRepository.saveAll(testimonials);
    }

    private void createDummyBlogs(UUID userId) {
        List<Blog> blogs = new ArrayList<>();

        Blog blog1 = new Blog();
        blog1.setUserId(userId);
        blog1.setTitle("The Future of Web Development: Trends to Watch in 2024");
        blog1.setSlug("future-web-development-trends-2024");
        blog1.setExcerpt("Explore the latest trends shaping the web development landscape, from AI integration to new JavaScript frameworks.");
        blog1.setContent("Web development is evolving at an unprecedented pace. In this comprehensive guide, we'll explore the key trends that are shaping the industry in 2024...\n\n## AI-Powered Development\n\nArtificial Intelligence is revolutionizing how we write code...\n\n## Progressive Web Apps\n\nPWAs continue to bridge the gap between web and mobile...\n\n## Serverless Architecture\n\nThe serverless revolution is changing how we think about backend development...");
        blog1.setFeaturedImageUrl("https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=800");
        blog1.setCategory("Web Development");
        blog1.setTags("[\"Web Development\",\"Trends\",\"JavaScript\",\"AI\",\"PWA\"]");
        blog1.setStatus("published");
        blog1.setPublishedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        blog1.setIsFeatured(true);
        blog1.setReadingTimeMinutes(8);
        blog1.setViewCount(1250);
        blog1.setSeoTitle("Future of Web Development: 2024 Trends & Technologies");
        blog1.setSeoDescription("Discover the top web development trends for 2024, including AI integration, PWAs, and serverless architecture.");
        blogs.add(blog1);

        Blog blog2 = new Blog();
        blog2.setUserId(userId);
        blog2.setTitle("Building Scalable React Applications: Best Practices");
        blog2.setSlug("building-scalable-react-applications");
        blog2.setExcerpt("Learn essential patterns and practices for building React applications that can grow with your business needs.");
        blog2.setContent("Scaling React applications requires careful planning and adherence to best practices. Here's what you need to know...\n\n## Component Architecture\n\nA well-structured component hierarchy is crucial...\n\n## State Management\n\nChoosing the right state management solution...\n\n## Performance Optimization\n\nTechniques to keep your React app fast and responsive...");
        blog2.setFeaturedImageUrl("https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800");
        blog2.setCategory("React");
        blog2.setTags("[\"React\",\"Scalability\",\"Best Practices\",\"Performance\"]");
        blog2.setStatus("published");
        blog2.setPublishedAt(LocalDateTime.of(2023, 12, 20, 14, 30));
        blog2.setReadingTimeMinutes(12);
        blog2.setViewCount(890);
        blogs.add(blog2);

        blogRepository.saveAll(blogs);
    }

    private void createDummyAchievements(UUID userId) {
        List<Achievement> achievements = new ArrayList<>();

        Achievement cert1 = new Achievement();
        cert1.setUserId(userId);
        cert1.setTitle("AWS Certified Solutions Architect");
        cert1.setDescription("Professional-level certification demonstrating expertise in designing distributed systems on AWS.");
        cert1.setIssuer("Amazon Web Services");
        cert1.setDateEarned(LocalDate.of(2023, 8, 15));
        cert1.setCategory("certificate");
        cert1.setCertificateUrl("https://aws.amazon.com/certification/");
        cert1.setVerificationUrl("https://verify.aws.com/cert123");
        cert1.setBadgeImageUrl("https://images.unsplash.com/photo-1606868306217-dbf5046868d2?w=200");
        cert1.setIsFeatured(true);
        cert1.setCredentialId("AWS-SAA-2023-001234");
        cert1.setSkills("[\"Cloud Architecture\",\"AWS Services\",\"Scalability\",\"Security\"]");
        cert1.setCompetencyLevel("advanced");
        cert1.setSortOrder(1);
        achievements.add(cert1);

        Achievement award1 = new Achievement();
        award1.setUserId(userId);
        award1.setTitle("Best Web Developer of the Year");
        award1.setDescription("Recognized for outstanding contribution to web development community and innovative project implementations.");
        award1.setIssuer("Tech Excellence Awards");
        award1.setDateEarned(LocalDate.of(2023, 11, 20));
        award1.setCategory("award");
        award1.setBadgeImageUrl("https://images.unsplash.com/photo-1567427017947-545c5f8d16ad?w=200");
        award1.setIsFeatured(true);
        award1.setSortOrder(2);
        achievements.add(award1);

        Achievement cert2 = new Achievement();
        cert2.setUserId(userId);
        cert2.setTitle("Google UX Design Professional Certificate");
        cert2.setDescription("Comprehensive program covering user experience design principles, research methods, and design tools.");
        cert2.setIssuer("Google");
        cert2.setDateEarned(LocalDate.of(2023, 5, 10));
        cert2.setCategory("certificate");
        cert2.setCertificateUrl("https://coursera.org/professional-certificates/google-ux-design");
        cert2.setBadgeImageUrl("https://images.unsplash.com/photo-1561070791-2526d30994b5?w=200");
        cert2.setCredentialId("GOOGLE-UX-2023-567890");
        cert2.setSkills("[\"User Research\",\"Wireframing\",\"Prototyping\",\"Design Thinking\"]");
        cert2.setCompetencyLevel("intermediate");
        cert2.setSortOrder(3);
        achievements.add(cert2);

        achievementRepository.saveAll(achievements);
    }

    private void createDummyTeamMembers(UUID userId) {
        List<TeamMember> teamMembers = new ArrayList<>();

        TeamMember member1 = new TeamMember();
        member1.setUserId(userId);
        member1.setName("Alice Smith");
        member1.setRole("Frontend Developer");
        member1.setBio("Passionate frontend developer with expertise in React and modern CSS. Loves creating beautiful and accessible user interfaces.");
        member1.setImageUrl("https://images.unsplash.com/photo-1494790108755-2616b612b786?w=300");
        member1.setSkills("[\"React\",\"TypeScript\",\"CSS3\",\"Sass\",\"Webpack\"]");
        member1.setExperienceYears(5);
        member1.setEmail("alice@johndoe.dev");
        member1.setLinkedinUrl("https://linkedin.com/in/alicesmith");
        member1.setDepartment("Development");
        member1.setLocation("San Francisco, CA");
        member1.setHourlyRate(new BigDecimal("85.00"));
        member1.setSortOrder(1);
        teamMembers.add(member1);

        TeamMember member2 = new TeamMember();
        member2.setUserId(userId);
        member2.setName("Bob Johnson");
        member2.setRole("Backend Developer");
        member2.setBio("Full-stack developer specializing in Node.js and database design. Expert in building scalable API architectures.");
        member2.setImageUrl("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=300");
        member2.setSkills("[\"Node.js\",\"MongoDB\",\"PostgreSQL\",\"Express.js\",\"Docker\"]");
        member2.setExperienceYears(6);
        member2.setEmail("bob@johndoe.dev");
        member2.setLinkedinUrl("https://linkedin.com/in/bobjohnson");
        member2.setDepartment("Development");
        member2.setLocation("Remote");
        member2.setHourlyRate(new BigDecimal("90.00"));
        member2.setSortOrder(2);
        teamMembers.add(member2);

        teamMemberRepository.saveAll(teamMembers);
    }

    private void createDummyEvents(UUID userId) {
        List<Event> events = new ArrayList<>();

        Event event1 = new Event();
        event1.setUserId(userId);
        event1.setTitle("Modern React Development Workshop");
        event1.setDescription("Hands-on workshop covering advanced React patterns, hooks, and performance optimization techniques.");
        event1.setEventDate(LocalDateTime.of(2024, 3, 15, 10, 0));
        event1.setEndDate(LocalDateTime.of(2024, 3, 15, 17, 0));
        event1.setLocation("San Francisco, CA");
        event1.setVenue("Tech Hub Conference Center");
        event1.setEventType("workshop");
        event1.setEventUrl("https://reactworkshop.johndoe.dev");
        event1.setRegistrationUrl("https://eventbrite.com/react-workshop");
        event1.setImageUrl("https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800");
        event1.setMaxAttendees(50);
        event1.setCurrentAttendees(35);
        event1.setPrice(new BigDecimal("299.00"));
        event1.setIsFeatured(true);
        event1.setOrganizerInfo("Organized by John Doe Development");
        event1.setAgenda("{\"10:00\":\"Introduction to Modern React\",\"12:00\":\"Lunch Break\",\"13:00\":\"Advanced Hooks\",\"15:00\":\"Performance Optimization\",\"17:00\":\"Q&A Session\"}");
        event1.setTargetAudience("Intermediate to Advanced React Developers");
        events.add(event1);

        Event event2 = new Event();
        event2.setUserId(userId);
        event2.setTitle("Web Development Fundamentals Webinar");
        event2.setDescription("Free webinar covering the basics of modern web development for beginners.");
        event2.setEventDate(LocalDateTime.of(2024, 2, 28, 19, 0));
        event2.setEndDate(LocalDateTime.of(2024, 2, 28, 20, 30));
        event2.setEventType("webinar");
        event2.setEventUrl("https://webinar.johndoe.dev");
        event2.setIsOnline(true);
        event2.setImageUrl("https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=800");
        event2.setMaxAttendees(200);
        event2.setCurrentAttendees(145);
        event2.setPrice(BigDecimal.ZERO);
        event2.setTargetAudience("Beginners");
        events.add(event2);

        eventRepository.saveAll(events);
    }

    private void createDummyPricingPlans(UUID userId) {
        List<PricingPlan> plans = new ArrayList<>();

        PricingPlan basic = new PricingPlan();
        basic.setUserId(userId);
        basic.setPlanName("Basic Website");
        basic.setDescription("Perfect for small businesses and personal websites");
        basic.setPrice(new BigDecimal("2500.00"));
        basic.setBillingPeriod("one-time");
        basic.setFeatures("[\"Responsive Design\",\"Up to 5 Pages\",\"Contact Form\",\"Basic SEO\",\"1 Month Support\"]");
        basic.setMaxRevisions(3);
        basic.setDeliveryTimeDays(14);
        basic.setIncludesSourceFiles(false);
        basic.setCategory("Website Development");
        basic.setSortOrder(1);
        plans.add(basic);

        PricingPlan standard = new PricingPlan();
        standard.setUserId(userId);
        standard.setPlanName("Standard Web App");
        standard.setDescription("Full-featured web application with custom functionality");
        standard.setPrice(new BigDecimal("5000.00"));
        standard.setBillingPeriod("one-time");
        standard.setFeatures("[\"Custom Design\",\"Up to 10 Pages\",\"Database Integration\",\"User Authentication\",\"Admin Panel\",\"3 Months Support\"]");
        standard.setIsPopular(true);
        standard.setMaxRevisions(5);
        standard.setDeliveryTimeDays(30);
        standard.setIncludesSourceFiles(true);
        standard.setCategory("Web Application");
        standard.setSortOrder(2);
        plans.add(standard);

        PricingPlan premium = new PricingPlan();
        premium.setUserId(userId);
        premium.setPlanName("Enterprise Solution");
        premium.setDescription("Complete enterprise-grade solution with advanced features");
        premium.setPrice(new BigDecimal("10000.00"));
        premium.setBillingPeriod("one-time");
        premium.setFeatures("[\"Custom Architecture\",\"Unlimited Pages\",\"API Development\",\"Third-party Integrations\",\"Performance Optimization\",\"6 Months Support\",\"Training Sessions\"]");
        premium.setMaxRevisions(10);
        premium.setDeliveryTimeDays(60);
        premium.setIncludesSourceFiles(true);
        premium.setIncludesCommercialUse(true);
        premium.setCategory("Enterprise");
        premium.setSortOrder(3);
        plans.add(premium);

        PricingPlan consulting = new PricingPlan();
        consulting.setUserId(userId);
        consulting.setPlanName("Technical Consulting");
        consulting.setDescription("Expert technical guidance and code reviews");
        consulting.setPrice(new BigDecimal("150.00"));
        consulting.setBillingPeriod("hourly");
        consulting.setFeatures("[\"Architecture Review\",\"Code Review\",\"Performance Analysis\",\"Technology Recommendations\",\"Best Practices Guidance\"]");
        consulting.setCategory("Consulting");
        consulting.setSortOrder(4);
        plans.add(consulting);

        pricingPlanRepository.saveAll(plans);
    }

    private void createDummyFAQs(UUID userId) {
        List<FAQ> faqs = new ArrayList<>();

        FAQ faq1 = new FAQ();
        faq1.setUserId(userId);
        faq1.setQuestion("What is your typical project timeline?");
        faq1.setAnswer("Project timelines vary depending on complexity and scope. A basic website typically takes 2-3 weeks, while a full web application can take 6-12 weeks. I'll provide a detailed timeline during our initial consultation based on your specific requirements.");
        faq1.setCategory("General");
        faq1.setIsFeatured(true);
        faq1.setSortOrder(1);
        faq1.setViewCount(156);
        faqs.add(faq1);

        FAQ faq2 = new FAQ();
        faq2.setUserId(userId);
        faq2.setQuestion("Do you provide ongoing maintenance and support?");
        faq2.setAnswer("Yes, I offer various support packages ranging from basic maintenance to comprehensive ongoing development. This includes security updates, bug fixes, feature enhancements, and technical support. Support terms are included with each project and can be extended as needed.");
        faq2.setCategory("Support");
        faq2.setIsFeatured(true);
        faq2.setSortOrder(2);
        faq2.setViewCount(98);
        faqs.add(faq2);

        FAQ faq3 = new FAQ();
        faq3.setUserId(userId);
        faq3.setQuestion("What technologies do you work with?");
        faq3.setAnswer("I specialize in modern web technologies including React, Node.js, MongoDB, PostgreSQL, and various cloud platforms like AWS. I stay current with the latest technologies and choose the best stack for each project based on requirements, scalability needs, and long-term maintenance considerations.");
        faq3.setCategory("Technical");
        faq3.setSortOrder(3);
        faq3.setViewCount(124);
        faqs.add(faq3);

        FAQ faq4 = new FAQ();
        faq4.setUserId(userId);
        faq4.setQuestion("How do you handle project payments?");
        faq4.setAnswer("I typically work with a 50% upfront payment to begin the project and 50% upon completion. For larger projects, we can arrange milestone-based payments. I accept various payment methods including bank transfers, PayPal, and Stripe. All payment terms are clearly outlined in the project contract.");
        faq4.setCategory("Billing");
        faq4.setSortOrder(4);
        faq4.setViewCount(87);
        faqs.add(faq4);

        FAQ faq5 = new FAQ();
        faq5.setUserId(userId);
        faq5.setQuestion("Can you help with hosting and domain setup?");
        faq5.setAnswer("Absolutely! I can help you choose the right hosting solution, set up your domain, configure SSL certificates, and deploy your application. I work with various hosting providers including AWS, Vercel, Netlify, and traditional shared hosting depending on your needs and budget.");
        faq5.setCategory("Technical");
        faq5.setSortOrder(5);
        faq5.setViewCount(76);
        faqs.add(faq5);

        faqRepository.saveAll(faqs);
    }

    private void createDummyAppointments(UUID userId) {
        List<Appointment> appointments = new ArrayList<>();

        Appointment apt1 = new Appointment();
        apt1.setUserId(userId);
        apt1.setClientName("Jennifer Martinez");
        apt1.setClientEmail("jennifer.martinez@example.com");
        apt1.setClientPhone("+1 (555) 987-6543");
        apt1.setAppointmentDate(LocalDateTime.of(2024, 2, 20, 14, 0));
        apt1.setEndTime(LocalDateTime.of(2024, 2, 20, 15, 0));
        apt1.setServiceType("Web Development Consultation");
        apt1.setStatus("confirmed");
        apt1.setNotes("Client needs a new e-commerce website for their boutique. Discuss features, timeline, and budget.");
        apt1.setMeetingType("video-call");
        apt1.setMeetingUrl("https://meet.google.com/abc-defg-hij");
        apt1.setDurationMinutes(60);
        apt1.setBookingSource("website");
        appointments.add(apt1);

        Appointment apt2 = new Appointment();
        apt2.setUserId(userId);
        apt2.setClientName("David Wilson");
        apt2.setClientEmail("david.wilson@techcorp.com");
        apt2.setClientPhone("+1 (555) 456-7890");
        apt2.setAppointmentDate(LocalDateTime.of(2024, 2, 22, 10, 0));
        apt2.setEndTime(LocalDateTime.of(2024, 2, 22, 11, 30));
        apt2.setServiceType("Technical Consulting");
        apt2.setStatus("pending");
        apt2.setNotes("Architecture review for existing application. Performance optimization consultation.");
        apt2.setMeetingType("in-person");
        apt2.setLocation("123 Tech Street, San Francisco, CA");
        apt2.setDurationMinutes(90);
        apt2.setPrice(new BigDecimal("225.00"));
        apt2.setBookingSource("referral");
        appointments.add(apt2);

        Appointment apt3 = new Appointment();
        apt3.setUserId(userId);
        apt3.setClientName("Lisa Thompson");
        apt3.setClientEmail("lisa.thompson@startup.io");
        apt3.setAppointmentDate(LocalDateTime.of(2024, 2, 25, 16, 0));
        apt3.setEndTime(LocalDateTime.of(2024, 2, 25, 17, 0));
        apt3.setServiceType("UI/UX Design Review");
        apt3.setStatus("confirmed");
        apt3.setNotes("Review current mobile app designs and provide feedback on user experience improvements.");
        apt3.setMeetingType("video-call");
        apt3.setDurationMinutes(60);
        apt3.setBookingSource("email");
        apt3.setFollowUpRequired(true);
        appointments.add(apt3);

        appointmentRepository.saveAll(appointments);
    }
}