(function($) {
    "use strict";
    jQuery(document).ready(function() {
        $("#status").fadeOut();
        $("#preloader").delay(200).fadeOut("slow");
        $("body").delay(200).css({
            "overflow": "visible"
        });
        new WOW().init();
    });
    $(document).on('click', '#back-to-top, .back-to-top', () =>{
        $('html, body').animate({
            scrollTop: 0
        },
        '500');
    return false;
});
$(window).on('scroll', () =>{
    if ($(window).scrollTop() > 500) {
        $('#back-to-top').fadeIn(200);
} else {
    $('#back-to-top').fadeOut(200);
}
});
$('.niceSelect').niceSelect();
if (($('.ct-search-link')).length > 0) {
    $('.ct-search-link').on('click',
    function(e) {
        e.preventDefault();
        $('.ct-searchForm').addClass('is-open');
    });
    $('.ct-searchForm-close').on('click',
    function(e) {
        $('.ct-searchForm').removeClass('is-open');
        e.preventDefault();
    })
}
jQuery(document).ready(() =>{
    jQuery('.js-video-button').modalVideo({
        channel: 'vimeo'
    });
});
$('.banner-slider').slick({
    dots: false,
    infinite: true,
    speed: 1500,
    autoplay: true,
    slidesToShow: 1,
    slidesToScroll: 1,
    responsive: [{
        breakpoint: 1024,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
            infinite: true,
            dots: true,
            arrows: false
        }
    },
    {
        breakpoint: 991,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: false
        }
    },
    {
        breakpoint: 480,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: false
        }
    }]
});
$('.partner-slider').slick({
    dots: false,
    infinite: true,
    speed: 1000,
    autoplay: true,
    arrows: false,
    slidesToShow: 5,
    slidesToScroll: 1,
    responsive: [{
        breakpoint: 991,
        settings: {
            slidesToShow: 2,
            slidesToScroll: 1,
            arrows: false
        }
    },
    {
        breakpoint: 600,
        settings: {
            slidesToShow: 2,
            slidesToScroll: 1,
            arrows: false
        }
    },
    {
        breakpoint: 480,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: false
        }
    }]
});
$('.review-slider').slick({
    dots: true,
    arrows: false,
    infinite: true,
    speed: 800,
    autoplay: true,
    slidesToShow: 2,
    slidesToScroll: 1,
    responsive: [{
        breakpoint: 1024,
        settings: {
            slidesToShow: 2,
            slidesToScroll: 1,
            infinite: true,
            dots: true
        }
    },
    {
        breakpoint: 991,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1
        }
    },
    {
        breakpoint: 480,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1
        }
    }]
});
$('.review-slider-2').slick({
    dots: false,
    arrows: false,
    infinite: true,
    speed: 800,
    autoplay: true,
    slidesToShow: 1,
    slidesToScroll: 1,
    responsive: [{
        breakpoint: 1024,
        settings: {
            slidesToShow: 2,
            slidesToScroll: 1,
            infinite: true,
            dots: true
        }
    },
    {
        breakpoint: 991,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1
        }
    },
    {
        breakpoint: 480,
        settings: {
            slidesToShow: 1,
            slidesToScroll: 1
        }
    }]
});
})(jQuery);