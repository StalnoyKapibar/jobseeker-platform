$(document).ready(function (e) {
    'use strict';
    feather.replace();
    window.onload = function () {
        /* Show animation */
        statisticAnimationProgress(2000);
    };

    function localDateTime(date) {
        return date.getFullYear().toString() + "-" + ((date.getMonth() + 1).toString().length == 2 ?
            (date.getMonth() + 1).toString() : "0" + (date.getMonth() + 1).toString()) + "-"
            + (date.getDate().toString().length == 2 ? date.getDate().toString() : "0" + date.getDate().toString()) + " "
            + (date.getHours().toString().length == 2 ? date.getHours().toString() : "0"
                + date.getHours().toString()) + ":" + (date.getMinutes().toString().length == 2 ?
                date.getMinutes().toString() : "0" + date.getMinutes().toString()) + ":"
            + (date.getSeconds().toString().length == 2 ? date.getSeconds().toString() : "0"
                + date.getSeconds().toString());
    };

    function getStartDate(days) {
        let date = new Date();
        date.setDate(date.getDate() - days);
        date.setHours(0, 0, 0, 0);
        return date;
    };

    function getEndDate(days) {
        let date = new Date();
        date.setDate(date.getDate() - days);
        if (days != 0) {
            date.setHours(23, 59, 59, 59);
        }
        return date;
    };

    function getStatisticsByAllPeriod(name) {
        let result;
        $.ajax({
            url: "/api/admin/all/" + name,
            type: "GET",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        return result
    };

    function getStatisticsByDatePeriod(name, start, end) {
        let result;
        let startDate = localDateTime(start);
        let endDate = localDateTime(end);
        $.ajax({
            url: "/api/admin/" + name,
            type: "GET",
            async: false,
            data: {
                "startDate": startDate,
                "endDate": endDate
            },
            success: function (data) {
                result = data;
            }
        });
        return result;
    };
    /* Counter */
    let $counter = $(".counter");

    let allSeekers = getStatisticsByAllPeriod("seekers");
    let $allSeekersCircleProgressBar = $('#all_seekers');
    $allSeekersCircleProgressBar.attr('data-percent', allSeekers);
    let $allSeekersProgress = $("#seekers_progress_bar");

    let todaySeekers = getStatisticsByDatePeriod("seekers", getStartDate(0), getEndDate(0));
    $('#today_seekers').attr('data-count', todaySeekers);
    let $todaySeekersProgress = $("#today_seekers_progress");

    let monthSeekers = getStatisticsByDatePeriod("seekers", getStartDate(30), getEndDate(0));
    $('#month_seekers').attr('data-count', monthSeekers);
    let $monthSeekersProgress = $('#month_seekers_progress');
    /* Resumes */
    let allResumes = getStatisticsByAllPeriod("resumes");
    let $allResumesCircleProgressBar = $('#all_resumes');
    $allResumesCircleProgressBar.attr('data-percent', allResumes);
    let $allResumesProgress = $('#resumes_progress_bar');

    let todayResumes = getStatisticsByDatePeriod("resumes", getStartDate(0), getEndDate(0));
    $('#today_resumes').attr('data-count', todayResumes);
    let $todayResumesProgress = $('#today_resumes_progress');

    let monthResumes = getStatisticsByDatePeriod("resumes", getStartDate(30), getEndDate(0));
    $('#month_resumes').attr('data-count', monthResumes);
    let $monthResumesProgress = $('#month_resumes_progress');
    /* Employers */
    let allEmployers = getStatisticsByAllPeriod("employers");
    let $allEmployersCircleProgressBar = $('#all_employers');
    $allEmployersCircleProgressBar.attr('data-percent', allEmployers);
    let $allEmployersProgress = $('#employers_progress_bar');

    let todayEmployers = getStatisticsByDatePeriod("employers", getStartDate(0), getEndDate(0));
    $('#today_employers').attr('data-count', todayEmployers);
    let $todayEmployersProgress = $('#today_employers_progress');

    let monthEmployers = getStatisticsByDatePeriod("employers", getStartDate(30), getEndDate(0));
    $('#month_employers').attr('data-count', monthEmployers);
    let $monthEmployersProgress = $('#month_employers_progress');
    /*Vacancies */
    let allVacancies = getStatisticsByAllPeriod("vacancies");
    let $allVacanciesCircleProgressBar = $('#all_vacancies');
    $allVacanciesCircleProgressBar.attr('data-percent', allVacancies);
    let $allVacanciesProgress = $('#vacancies_progress_bar');

    let todayVacancies = getStatisticsByDatePeriod("vacancies", getStartDate(0), getEndDate(0));
    $('#today_vacancies').attr('data-count', todayVacancies);
    let $todayVacanciesProgress = $('#today_vacancies_progress');

    let monthVacancies = getStatisticsByDatePeriod("vacancies", getStartDate(30), getEndDate(0));
    $('#month_vacancies').attr('data-count', monthVacancies);
    let $monthVacanciesProgress = $('#month_vacancies_progress');

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
    function counterAnimation(item, duration) {
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
    function setLabelsX(labels) {
        currentLabels.push(daysNames[(currentNumberDay - 1)]);
        let step = currentNumberDay - 1;
        for (step; step > 0; step--) {
            labels.unshift(daysNames[step - 1]);
        }
        for (let k = 7; k > currentNumberDay; k--) {
            labels.unshift(daysNames[k - 1]);
        }
        return labels;
    };

    function setDataYForLastWeek(name) {
        let dataY = [];
        for (let i = 0; i < 7; i++) {
            dataY[6 - i] = getStatisticsByDatePeriod(name, getStartDate(i), getEndDate(i));
        }
        return dataY;
    }

    let currentDateTime = new Date();
    var daysNames = ["Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"];
    let currentNumberDay = currentDateTime.getDay();
    let currentLabels = [];
    setLabelsX(currentLabels);
    /* Diagramm about dynamic seekers by last week*/
    let $ctxSeekers = $('#dynamic_seekers')[0].getContext('2d');
    let seekersChart = new Chart($ctxSeekers, {
        type: 'bar',
        data: {
            labels: currentLabels,
            datasets: [{
                data: setDataYForLastWeek("seekers"),
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
                        beginAtZero: true,
                        stepSize: 1,
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
    /* Diagramm about dynamic employers by last week */
    let $ctxEmployers = $('#dynamic_employers')[0].getContext('2d');
    let employersChart = new Chart($ctxEmployers, {
        type: 'bar',
        data: {
            labels: currentLabels,
            datasets: [{
                data: setDataYForLastWeek("employers"),
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
                        beginAtZero: true,
                        stepSize: 1,
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

    /* diagrams about % tags in resumes and vacancies */
    function getTagsLabelsForDoughnutDiagram() {
        let pieTagsLabels = [];
        $.ajax({
            url: "/api/admin/all/tags",
            type: "GET",
            async: false,
            success: function (data) {
                $.each(data, function (i, tag) {
                    pieTagsLabels.push(data[i].name);
                });
            }
        });
        return pieTagsLabels;
    };

    function getStatisticsByTags(name, size) {
        let result = new Array();
        let tagsName = getTagsLabelsForDoughnutDiagram();
        for (let i = 0; i < tagsName.length; i++) {
            $.ajax({
                url: "/api/admin/" + name + "/tag/" + tagsName[i],
                type: "GET",
                async: false,
                success: function (data) {
                    result.push({"name": tagsName[i], "value": data})
                },
                error: function () {
                    console.log("Hello");
                }
            });
        }
        result.sort(function (a, b) {
            return b.value - a.value;
        });
        let othersSum = result[size].value;
        if (tagsName.length >= size + 1) {
            for (let i = 1; i < tagsName.length - (size - 2); i++) {
                othersSum += result[result.length - 1].value;
                result[size] = {"name": "Others", "value": othersSum};
            }
        }
        return result.slice(0, size + 1);
    };

    function getSortedLabelsForDoughnutDiagram(name, size) {
        let result = [];
        let data = getStatisticsByTags(name, size);
        for (let i = 0; i < data.length; i++) {
            result[i] = data[i].name;
        }
        return result;
    }

    function getAllDataForSortedLabels(name, size) {
        let result = [];
        let data = getStatisticsByTags(name, size);
        for (let i = 0; i < data.length; i++) {
            result[i] = data[i].value;
        }
        return result;
    }

    /* Diagram about tags in resumes */
    google.charts.load("visualization", "1", {packages: ["corechart"]});
    google.charts.setOnLoadCallback(drawChart1);

    function drawChart1() {
        let tags = getSortedLabelsForDoughnutDiagram("resumes", 9);
        let resumes = getAllDataForSortedLabels("resumes", 9);
        let data = new google.visualization.DataTable();
        data.addColumn('string', 'tags');
        data.addColumn('number', 'resumes');
        for (let i = 0; i < tags.length; i++)
            data.addRow([tags[i], resumes[i]]);
        let options = {
            chartArea: {left: 20, top: 20, width: "100%", height: "100%"}
        };
        let chart = new google.visualization.PieChart(document.getElementById('donutchart_resumes'));
        chart.draw(data, options);
    }

    /* Diagram about tags in vacancies */
    google.load("visualization", "1", {packages: ["corechart"]});
    google.setOnLoadCallback(drawChart2);

    function drawChart2() {
        let tags = getSortedLabelsForDoughnutDiagram("vacancies", 9);
        let vacancies = getAllDataForSortedLabels("vacancies", 9);
        let data = new google.visualization.DataTable();
        data.addColumn('string', 'tags');
        data.addColumn('number', 'vacancies');
        for (let i = 0; i < tags.length; i++)
            data.addRow([tags[i], vacancies[i]]);
        let options = {
            chartArea: {left: 20, top: 20, width: "100%", height: "100%"}
        };
        let chart = new google.visualization.PieChart(document.getElementById('donutchart_vacancies'));
        chart.draw(data, options);
    }

    $(window).resize(function () {
        drawChart1();
        drawChart2();
    });
});