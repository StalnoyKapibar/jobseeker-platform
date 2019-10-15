$(document).ready(function (e) {
    'use strict';
    feather.replace();
    let $counter = $(".counter");
    /* Seekers */
    let allSeekers = 50;
    let $allSeekersCircleProgressBar = $('#all_seekers');
    $allSeekersCircleProgressBar.attr('data-percent', allSeekers);
    let $allSeekersProgress = $("#seekers_progress_bar");

    let todaySeekers = 10;
    $('#today_seekers').attr('data-count', todaySeekers);
    let $todaySeekersProgress = $("#today_seekers_progress");

    let monthSeekers = 25;
    $('#month_seekers').attr('data-count', monthSeekers );
    let $monthSeekersProgress = $('#month_seekers_progress');
    /* Resumes */
    let allResumes = 40;
    let $allResumesCircleProgressBar = $('#all_resumes');
    $allResumesCircleProgressBar.attr('data-percent', allResumes);
    let $allResumesProgress = $('#resumes_progress_bar');

    let todayResumes = 6;
    $('#today_resumes').attr('data-count', todayResumes);
    let $todayResumesProgress = $('#today_resumes_progress');

    let monthResumes = 17;
    $('#month_resumes').attr('data-count', monthResumes);
    let $monthResumesProgress = $('#month_resumes_progress');
    /* Employers */
    let allEmployers = 129;
    let $allEmployersCircleProgressBar = $('#all_employers');
    $allEmployersCircleProgressBar.attr('data-percent', allEmployers);
    let $allEmployersProgress = $('#employers_progress_bar');

    let todayEmployers = 3;
    $('#today_employers').attr('data-count', todayEmployers);
    let $todayEmployersProgress = $('#today_employers_progress');

    let monthEmployers = 59;
    $('#month_employers').attr('data-count', monthEmployers);
    let $monthEmployersProgress = $('#month_employers_progress');
    /*Vacancies */
    let allVacancies = 155;
    let $allVacanciesCircleProgressBar = $('#all_vacancies');
    $allVacanciesCircleProgressBar.attr('data-percent', allVacancies);
    let $allVacanciesProgress = $('#vacancies_progress_bar');

    let todayVacancies = 16;
    $('#today_vacancies').attr('data-count', todayVacancies);
    let $todayVacanciesProgress = $('#today_vacancies_progress');

    let monthVacancies = 93;
    $('#month_vacancies').attr('data-count', monthVacancies);
    let $monthVacanciesProgress = $('#month_vacancies_progress');

    /* Show animation */
    statisticAnimationProgress(1000);
    /* Animate circle, linear progress bar  with counting */
    function statisticAnimationProgress(duration) {
        allCircleProgressAnimation(duration);
        allAnimationProgressBar(duration);
        counterAnimation($counter, duration);
    };
    /* Circle progress bar */
    /* Using circle-progress.js plugin*/
    function allCircleProgressAnimation(duration) {
        animateCircularProgressBar($allSeekersProgress, $allSeekersCircleProgressBar, allSeekers, duration);
        animateCircularProgressBar($allResumesProgress, $allResumesCircleProgressBar, allResumes, duration);
        animateCircularProgressBar($allEmployersProgress, $allEmployersCircleProgressBar, allEmployers, duration);
        animateCircularProgressBar($allVacanciesProgress, $allVacanciesCircleProgressBar, allVacancies, duration);
    };
    function animateCircularProgressBar(progressBar, circle, value, duration) {
        progressBar.each(function () {
            progressBar.data('animate', true);
            progressBar.find(circle).circleProgress({
                startAngle: -Math.PI / 2,
                value: 1,
                size: 200,
                thickness: 2,
                fill: {
                    color: '#ffbb33'
                },
                animation: {
                    duration: duration,
                    easing: "circleProgressEasing"
                }
            }).on('circle-animation-progress', function (progress, stepValue) {
                $(this).find('span').text((stepValue * value).toFixed(0));
            }).stop();
        });
    };
    /* Linear progress bar */
    function calculatorForProgressBarLength(value1, value2) {
        let lengthResult = (value2 / (value1 / 100));
        return lengthResult;
    };
    function animateProgressBarLength(item, value1, value2, duration) {
        item.animate({
            width: calculatorForProgressBarLength(value1, value2) + "%"
        }, duration);
    };
    function allAnimationProgressBar(duration) {
        animateProgressBarLength($todaySeekersProgress, allSeekers, todaySeekers, duration);
        animateProgressBarLength($monthSeekersProgress, allSeekers, monthSeekers, duration);
        animateProgressBarLength($todayResumesProgress, allResumes, todayResumes, duration);
        animateProgressBarLength($monthResumesProgress, allResumes, monthResumes, duration);
        animateProgressBarLength($todayEmployersProgress, allEmployers, todayEmployers, duration);
        animateProgressBarLength($monthEmployersProgress, allEmployers, monthEmployers, duration);
        animateProgressBarLength($todayVacanciesProgress, allVacancies, todayVacancies, duration);
        animateProgressBarLength($monthVacanciesProgress, allVacancies, monthVacancies, duration);
    };
    /*Counting animation */
    function counterAnimation(item, duration){
        item.each(function () {
            let $this = $(this),
                countTo = $this.attr('data-count');

            $({
                countNum: $this.text()
            }).animate({
                countNum: countTo
            }, {
                duration: duration,
                easing: 'linear',
                step: function () {
                    $this.text(Math.floor(this.countNum));
                },
                complete: function () {
                    $this.text(this.countNum);
                }
            });
        });
    };
    /* Dynamic graphics */
    let currentDateTime = new Date();
    var daysNames = ["Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"];
    let currentNumberDay = currentDateTime.getDay();
    let currentDate = currentDateTime.getDate();
    let currentMonth = currentDateTime.getMonth();
    let currentYear = currentDateTime.getUTCFullYear();
    let currentLabels = [];
    setLabels(currentLabels);
    console.log(currentLabels);
    /* Diagramm about dynamic seekers */
    let $ctxSeekers = $('#dynamic_seekers')[0].getContext('2d');
    let seekersChart = new Chart($ctxSeekers, {
        type: 'bar',
        data: {
            labels: currentLabels,
            datasets: [{
                data: [12, 19, 3, 5, 2, 3, 4],
                backgroundColor: [
                    'rgba(0, 123, 255, 1)',
                    'rgba(23, 162, 184, 1)',
                    'rgba(220, 53, 69, 1)',
                    'rgba(40, 167, 69, 1)',
                    'rgba(52, 58, 64, 1)',
                    'rgba(108, 117, 125, 1)',
                    'rgba(255, 193, 7, 1)'
                ],
                borderColor: [
                    'rgba(0, 123, 255, 1)',
                    'rgba(23, 162, 184, 1)',
                    'rgba(220, 53, 69, 1)',
                    'rgba(40, 167, 69, 1)',
                    'rgba(52, 58, 64, 1)',
                    'rgba(108, 117, 125, 1)',
                    'rgba(255, 193, 7, 1)'
                ],
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
            legend: {
                display: false,
                labels: {
                    fontColor: 'rgb(255, 99, 132)'
                }
            }
        },
    });
    /* Diagramm about dynamic employers */
    let $ctxEmployers = $('#dynamic_employers')[0].getContext('2d');
    let employersChart = new Chart($ctxEmployers, {
        type: 'bar',
        data: {
            labels: currentLabels,
            datasets: [{
                data: [12, 19, 3, 5, 2, 3, 4],
                backgroundColor: [
                    'rgba(0, 123, 255, 1)',
                    'rgba(23, 162, 184, 1)',
                    'rgba(220, 53, 69, 1)',
                    'rgba(40, 167, 69, 1)',
                    'rgba(52, 58, 64, 1)',
                    'rgba(108, 117, 125, 1)',
                    'rgba(255, 193, 7, 1)'
                ],
                borderColor: [
                    'rgba(0, 123, 255, 1)',
                    'rgba(23, 162, 184, 1)',
                    'rgba(220, 53, 69, 1)',
                    'rgba(40, 167, 69, 1)',
                    'rgba(52, 58, 64, 1)',
                    'rgba(108, 117, 125, 1)',
                    'rgba(255, 193, 7, 1)'
                ],
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
            legend: {
                display: false,
                labels: {
                    fontColor: 'rgb(255, 99, 132)'
                }
            }
        },
    });
    /* Diagramm about tags in resumes */
    let $ctxResumes = $('#dynamic_resumes')[0].getContext('2d');
    var resumesChart = new Chart($ctxResumes, {
        type: 'pie',
        data: {
            labels: [],
            datasets: [{
                data: [300, 50, 100, 40, 120],
                backgroundColor: ["#F7464A", "#46BFBD", "#FDB45C", "#949FB1", "#4D5360"],
                hoverBackgroundColor: ["#FF5A5E", "#5AD3D1", "#FFC870", "#A8B3C5", "#616774"]
            }]
        },
        options: {
            responsive: true
        }
    });
    /* Diagramm about tags in vacancies */
    let $ctxVacancies = $('#dynamic_vacancies')[0].getContext('2d');
    var vacanciesChart = new Chart($ctxVacancies, {
        type: 'pie',
        data: {
            labels: [],
            datasets: [{
                data: [300, 50, 100, 40, 120],
                backgroundColor: ["#F7464A", "#46BFBD", "#FDB45C", "#949FB1", "#4D5360"],
                hoverBackgroundColor: ["#FF5A5E", "#5AD3D1", "#FFC870", "#A8B3C5", "#616774"]
            }]
        },
        options: {
            responsive: true
        }
    });
    function setLabels(labels){
        // currentLabels.push(daysNames[(currentNumberDay-1)] +" "+ currentYear.toString() + "-" + currentMonth.toString() + "-" + currentDate.toString());
        currentLabels.push(daysNames[(currentNumberDay-1)]);
        let step = currentNumberDay - 1;
        for(step; step > 0; step-- ){
            labels.unshift(daysNames[step - 1]);
        }
        for(let k = 7; k >currentNumberDay; k--){
            labels.unshift(daysNames[k-1]);
        }
        return labels;
    }
});